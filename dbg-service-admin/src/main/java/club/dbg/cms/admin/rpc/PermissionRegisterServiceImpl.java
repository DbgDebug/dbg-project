package club.dbg.cms.admin.rpc;

import club.dbg.cms.admin.config.RoleConfig;
import club.dbg.cms.admin.dao.PermissionMapper;
import club.dbg.cms.admin.dao.RolePermissionMapper;
import club.dbg.cms.admin.service.rediscache.PermissionCacheService;
import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.domain.admin.RolePermissionDO;
import club.dbg.cms.domain.admin.ServiceDO;
import club.dbg.cms.admin.dao.ServiceMapper;
import club.dbg.cms.rpc.PermissionRegisterService;
import club.dbg.cms.rpc.pojo.PermissionRegisterDTO;
import club.dbg.cms.util.MD5;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dbg
 */
@Service
public class PermissionRegisterServiceImpl implements PermissionRegisterService {
    private static final Logger log = LoggerFactory.getLogger(PermissionRegisterServiceImpl.class);

    private final String permissionKey;

    private final RoleConfig roleConfig;

    private final PermissionMapper permissionMapper;

    private final ServiceMapper serviceMapper;

    private final RolePermissionMapper rolePermissionMapper;

    private final PermissionCacheService permissionCacheService;

    public PermissionRegisterServiceImpl(
            @Value("${system.permissionKey}")
                    String permissionKey, RoleConfig roleConfig,
            PermissionMapper permissionMapper,
            ServiceMapper serviceMapper,
            RolePermissionMapper rolePermissionMapper,
            PermissionCacheService permissionCacheService) {
        this.permissionKey = permissionKey;
        this.roleConfig = roleConfig;
        this.permissionMapper = permissionMapper;
        this.serviceMapper = serviceMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.permissionCacheService = permissionCacheService;
    }

    /**
     * 验证需要注册权限的服务是否已添加到服务表，没有则不能注册
     * 检测注册权限是否已存在，存在则更新，不存在则添加
     * 当新注册的权限与该服务原有注册权限不一致时，将不存在于该次注册中的旧权限设置为停用
     * 新加权限默认赋予超级管理员
     * 刷新redis权限缓存
     *
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean permissionRegister(PermissionRegisterDTO permissionRegisterDTO) {
        if (!MD5.md5(permissionRegisterDTO.getServiceName()
                + JSON.toJSONString(permissionRegisterDTO.getPermissionList())
                + permissionKey).equals(permissionRegisterDTO.getSign())) {
            log.info("数据签名错误");
            return false;
        }
        log.info("permissions:{}", JSON.toJSONString(permissionRegisterDTO));
        ServiceDO serviceInfo = serviceMapper.selectServiceByName(permissionRegisterDTO.getServiceName());
        if (serviceInfo == null) {
            log.info("{}服务不存在", permissionRegisterDTO.getServiceName());
            return false;
        }
        List<PermissionDO> permissionsDb = permissionMapper.selectByServiceId(serviceInfo.getId());
        List<PermissionDO> insertList = new ArrayList<>();
        // 注册权限的服务未拥有任何权限
        if (permissionsDb == null || permissionsDb.isEmpty()) {
            for (PermissionDO permission : permissionRegisterDTO.getPermissionList()) {
                permission.setServiceId(serviceInfo.getId());
                permission.setCreateTime(System.currentTimeMillis() / 1000);
                permission.setUpdateTime(permission.getCreateTime());
                permission.setStatus(1);
                insertList.add(permission);
            }
            permissionMapper.insertPermissions(insertList);
            superAdminAuthorize(insertList);
            permissionCacheService.refreshPermissionCache();
            log.info("新权限添加成功");
            return true;
        }
        List<PermissionDO> updateList = new ArrayList<>();
        for (PermissionDO permission : permissionRegisterDTO.getPermissionList()) {
            boolean contains = false;
            for (PermissionDO pdb : permissionsDb) {
                if (pdb.getPermissionId().equals(permission.getPermissionId())) {
                    contains = true;
                    if (pdb.getPermissionName().equals(permission.getPermissionName())
                            && pdb.getPath().equals(permission.getPath())) {
                        continue;
                    }
                    if (pdb.getPermissionName().equals(permission.getPermissionName())) {
                        pdb.setPermissionName(null);
                    }
                    if (pdb.getPath().equals(permission.getPath())) {
                        pdb.setPath(null);
                    }
                    permission.setId(pdb.getId());
                    permission.setUpdateTime(System.currentTimeMillis() / 1000);
                    updateList.add(permission);
                    break;
                }
            }
            if (!contains) {
                permission.setServiceId(serviceInfo.getId());
                permission.setCreateTime(System.currentTimeMillis() / 1000);
                permission.setUpdateTime(permission.getCreateTime());
                permission.setStatus(1);
                insertList.add(permission);
            }
        }
        if (!insertList.isEmpty()) {
            permissionMapper.insertPermissions(insertList);
            superAdminAuthorize(insertList);
            log.info("新权限添加成功");
        }
        if (!updateList.isEmpty()) {
            permissionMapper.updatePermissions(updateList);
            log.info("权限更新成功");
        }
        // 取差集，status更新为0
        if (permissionsDb.removeAll(permissionRegisterDTO.getPermissionList())) {
            if (!permissionsDb.isEmpty()) {
                for (PermissionDO permissionDO : permissionsDb) {
                    permissionDO.setPermissionName(null);
                    permissionDO.setPath(null);
                    permissionDO.setStatus(0);
                }
                permissionMapper.updatePermissions(permissionsDb);
            }
        }
        permissionCacheService.refreshPermissionCache();
        return true;
    }

    /**
     * 将权限赋予超级管理员
     *
     * @param permissionDOS 权限列表
     */
    private void superAdminAuthorize(List<PermissionDO> permissionDOS) {
        List<RolePermissionDO> rolePermissionList = new ArrayList<>();
        for (PermissionDO permissionDO : permissionDOS) {
            RolePermissionDO rolePermissionDO = new RolePermissionDO();
            rolePermissionDO.setRoleId(roleConfig.getSuperAdministratorId());
            rolePermissionDO.setPermissionId(permissionDO.getId());
            rolePermissionList.add(rolePermissionDO);
        }
        if (!rolePermissionList.isEmpty()) {
            rolePermissionMapper.insertRolePermissions(rolePermissionList);
            log.info("权限已赋予超级管理员");
        }
        permissionCacheService.refreshRoleCache(roleConfig.getSuperAdministratorId());
    }
}
