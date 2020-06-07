package club.dbg.cms.admin.service.rediscache;

import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.admin.dao.PermissionMapper;
import club.dbg.cms.admin.dao.RoleMapper;
import club.dbg.cms.admin.redis.RedisUtils;
import club.dbg.cms.rpc.pojo.RoleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dbg
 */
@Service
public class PermissionCacheServiceImpl implements PermissionCacheService {
    private static final Logger log = LoggerFactory.getLogger(PermissionCacheService.class);

    private final RedisUtils redisUtils;

    private final RoleMapper roleMapper;

    private final PermissionMapper permissionMapper;

    public PermissionCacheServiceImpl(RedisUtils redisUtils,
                                      RoleMapper roleMapper,
                                      PermissionMapper permissionMapper,
                                      @Value("${redis.cache.permissionHeader}")
                                              String permissionHeader,
                                      @Value("${redis.cache.roleHeader}")
                                              String roleHeader,
                                      @Value("${redis.cache.timeOut}")
                                              Integer cacheTimeOut) {
        this.redisUtils = redisUtils;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
        this.permissionHeader = permissionHeader;
        this.roleHeader = roleHeader;
        this.cacheTimeOut = cacheTimeOut;
    }

    private final String permissionHeader;


    private final String roleHeader;

    private final Integer cacheTimeOut;

    @Scheduled(cron = "0 0 * * * ?")
    @Override
    public void init() {
        this.roleCache();
        this.permissionCache();
    }

    @Override
    public Boolean refreshRoleCache(Integer roleId) {
        RoleDTO roleDTO = roleMapper.selectRoleDTOById(roleId);
        if (roleDTO == null) {
            log.info("roleId:{}不存在，刷新redis缓存失败", roleId);
            return false;
        }
        if (!redisUtils.set(roleHeader + roleId, roleDTO, cacheTimeOut)) {
            log.info("角色{}写入redis失败，刷新redis缓存失败", roleDTO.getRoleName());
            return false;
        }
        log.info("角色{}写入redis成功，刷新redis缓存成功", roleDTO.getRoleName());
        return true;
    }

    @Override
    public Boolean deleteRoleCache(Integer roleId) {
        return redisUtils.delete(roleHeader + roleId);
    }

    @Override
    public Boolean refreshPermissionCache() {
        int status = 1;
        List<PermissionDO> permissionList = permissionMapper.selectPermissionByStatus(status);
        if (permissionList.isEmpty()) {
            log.info("权限表为空，刷新redis缓存失败");
            return false;
        }
        Map<String, Integer> permissionMap = new HashMap<>(100);
        for (PermissionDO permissionDO : permissionList) {
            if (permissionDO.getStatus() != 1) {
                continue;
            }
            permissionMap.put(permissionDO.getPermissionId(), permissionDO.getId());
        }
        if (!redisUtils.hashPutAll(permissionHeader, permissionMap)) {
            log.info("权限表写入redis失败，刷新redis缓存失败");
            return false;
        }

        return true;
    }

    private void roleCache() {
        List<RoleDTO> roleList = roleMapper.selectRoleList("", 0, 1000);
        if (roleList.isEmpty()) {
            log.info("角色表为空，初始化角色缓存失败");
            return;
        }
        for (RoleDTO roleDTO : roleList) {
            if (roleDTO.getStatus() == 1) {
                if (!redisUtils.set(roleHeader + roleDTO.getId(), roleDTO, 3600)) {
                    log.info("角色{}写入redis失败", roleDTO.getRoleName());
                }
            }
        }
    }

    private void permissionCache() {
        int status = 1;
        List<PermissionDO> permissionList = permissionMapper.selectPermissionByStatus(status);
        if (permissionList.isEmpty()) {
            log.info("权限表为空，写入redis失败");
            return;
        }
        Map<String, Integer> permissionMap = new HashMap<>(100);
        for (PermissionDO permissionDO : permissionList) {
            if (permissionDO.getStatus() != 1) {
                continue;
            }
            permissionMap.put(permissionDO.getPermissionId(), permissionDO.getId());
        }
        if (!redisUtils.hashPutAll(permissionHeader, permissionMap)) {
            log.info("初始化角色缓存失败");
        }
    }
}
