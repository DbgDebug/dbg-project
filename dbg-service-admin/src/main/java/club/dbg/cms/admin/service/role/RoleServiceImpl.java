package club.dbg.cms.admin.service.role;

import club.dbg.cms.domain.admin.AccountRoleDO;
import club.dbg.cms.domain.admin.RoleDO;
import club.dbg.cms.domain.admin.RolePermissionDO;
import club.dbg.cms.admin.dao.AccountRoleMapper;
import club.dbg.cms.admin.dao.RoleMapper;
import club.dbg.cms.admin.dao.RolePermissionMapper;
import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.account.pojo.AccountDTO;
import club.dbg.cms.admin.service.rediscache.PermissionCacheService;
import club.dbg.cms.admin.service.role.pojo.RoleListDTO;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.RoleDTO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author dbg
 */
@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final DataSourceTransactionManager transactionManager;

    private final RoleMapper roleMapper;

    private final AccountRoleMapper accountRoleMapper;

    private final RolePermissionMapper rolePermissionMapper;

    private final PermissionCacheService permissionCacheService;

    public RoleServiceImpl(
            RoleMapper roleMapper,
            AccountRoleMapper accountRoleMapper,
            RolePermissionMapper rolePermissionMapper,
            DataSourceTransactionManager transactionManager,
            PermissionCacheService permissionCacheService) {
        this.roleMapper = roleMapper;
        this.accountRoleMapper = accountRoleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.transactionManager = transactionManager;
        this.permissionCacheService = permissionCacheService;
    }

    @Override
    public RoleListDTO getRoleList(String roleName, int page, int pageSize) {
        page = (page - 1) * pageSize;
        RoleListDTO roleList = new RoleListDTO();
        roleList.setRoleList(roleMapper.selectRoleList(roleName, page, pageSize));
        roleList.setTotal(roleMapper.selectRoleCount(roleName));
        return roleList;
    }

    @Override
    synchronized public Boolean addRole(Operator operator, RoleDTO role) {
        RoleDO roleCheck = roleMapper.selectRoleByRoleName(role.getRoleName());
        if (roleCheck != null) {
            throw new BusinessException("角色名已存在");
        }
        if (role.getRoleLevel() <= operator.getRoleLevel()) {
            throw new BusinessException("角色等级设置错误");
        }

        // 判断操作人员是否拥有所操作权限的权限
        HashSet<Integer> operatorPermissionList = rolePermissionMapper.selectPermissionIdByRoleIds(operator.getRoleIds());
        if (!checkRolePermission(operatorPermissionList, role.getPermissionSet())) {
            throw new BusinessException("未拥有的权限无法操作");
        }

        RoleDO roleDO = new RoleDO();
        roleDO.setCreatorId(operator.getRoleId());
        roleDO.setRoleName(role.getRoleName());
        roleDO.setRoleLevel(role.getRoleLevel());
        roleDO.setStatus(role.getStatus());
        roleDO.setCreateTime(System.currentTimeMillis() / 1000);
        roleDO.setUpdateTime(roleDO.getCreateTime());

        // 手动事务管理
        TransactionStatus transStatus = this.getTransactionStatus();
        try {
            if (roleMapper.insertRole(roleDO) != 1) {
                log.info("添加角色，写入数据错误");
                throw new BusinessException("添加角色失败");
            }
            role.setId(roleDO.getId());
            if (rolePermissionMapper.insertRolePermissions(getRolePermissionDOList(role)) != role.getPermissionSet().size()) {
                log.info("添加角色，写入权限错误");
                throw new BusinessException("添加角色失败");
            }
            transactionManager.commit(transStatus);
        } catch (Exception e) {
            log.warn("添加角色异常:", e);
            transactionManager.rollback(transStatus);
            throw e;
        }
        permissionCacheService.refreshRoleCache(roleDO.getId());
        return true;
    }

    /**
     * TODO
     * 刷新redis缓存
     */
    @Override
    synchronized public Boolean editRole(Operator operator, RoleDTO roleDTO) {
        RoleDO roleCheck = roleMapper.selectRoleByRoleName(roleDTO.getRoleName());
        if (roleCheck != null && !roleCheck.getId().equals(roleDTO.getId())) {
            throw new BusinessException("角色名已存在");
        }
        roleCheck = roleMapper.selectRoleById(roleDTO.getId());
        if (roleCheck == null) {
            throw new BusinessException("编辑角色不存在");
        }
        if (roleCheck.getRoleLevel() < operator.getRoleLevel()) {
            throw new BusinessException("无权限编辑此角色");
        }
        // 角色等级相同，操作角色不是被操作角色的创建者时
        if (roleCheck.getRoleLevel().equals(operator.getRoleLevel())
                && !roleCheck.getCreatorId().equals(operator.getRoleId())) {
            throw new BusinessException("无权限编辑此角色");
        }
        if (roleDTO.getRoleLevel() <= operator.getRoleLevel()) {
            throw new BusinessException("角色等级设置错误");
        }

        // 判断操作人员是否拥有所操作权限的权限
        HashSet<Integer> operatorPermissionSet = rolePermissionMapper.selectPermissionIdByRoleIds(operator.getRoleIds());
        if (!checkRolePermission(operatorPermissionSet, roleDTO.getPermissionSet())) {
            throw new BusinessException("未拥有的权限无法操作");
        }
        boolean isUpdate = false;
        TransactionStatus transStatus = this.getTransactionStatus();
        try {
            if (!(roleCheck.getRoleName().equals(roleDTO.getRoleName())
                    && roleCheck.getRoleLevel().equals(roleDTO.getRoleLevel())
                    && roleCheck.getStatus().equals(roleDTO.getStatus()))) {
                RoleDO roleDO = new RoleDO();
                roleDO.setId(roleDTO.getId());
                roleDO.setRoleName(roleDTO.getRoleName());
                roleDO.setRoleLevel(roleDTO.getRoleLevel());
                roleDO.setStatus(roleDTO.getStatus());
                roleDO.setUpdateTime(System.currentTimeMillis() / 1000);
                if (roleMapper.updateRole(roleDO) != 1) {
                    throw new BusinessException("编辑角色失败");
                }
                isUpdate = true;
            }

            List<RolePermissionDO> rolePermissionDOS = rolePermissionMapper.selectByRoleId(roleDTO.getId());
            HashSet<Integer> rolePermissionSetOld = new HashSet<>();
            for(RolePermissionDO rolePermissionDO : rolePermissionDOS) {
                rolePermissionSetOld.add(rolePermissionDO.getPermissionId());
            }
            // 判断权限是否相同，相同则不进行修改
            if (rolePermissionSetOld.size() != roleDTO.getPermissionSet().size()
                || !checkRolePermission(rolePermissionSetOld, roleDTO.getPermissionSet())) {
                HashSet<Integer> rolePermissionSetNew = roleDTO.getPermissionSet();
                // 新权限对旧权限取差集，写入差集
                HashSet<Integer> differenceSet = new HashSet<>();
                for (Integer permissionId : roleDTO.getPermissionSet()) {
                    if(!rolePermissionSetOld.contains(permissionId)){
                        differenceSet.add(permissionId);
                    }
                }
                roleDTO.setPermissionSet(differenceSet);
                if (!roleDTO.getPermissionSet().isEmpty()) {
                    if (rolePermissionMapper.insertRolePermissions(getRolePermissionDOList(roleDTO)) < 0) {
                        log.warn("插入新权限错误");
                        throw new BusinessException("编辑角色失败");
                    }
                }
                // 旧权限对新权限取差集，删除差集
                rolePermissionSetOld.removeAll(rolePermissionSetNew);
                rolePermissionDOS.removeIf(rolePermission -> !rolePermissionSetOld.contains(rolePermission.getPermissionId()));
                HashSet<Integer> rolePermissionIds = new HashSet<>();
                for(RolePermissionDO rolePermissionDO : rolePermissionDOS) {
                    rolePermissionIds.add(rolePermissionDO.getId());
                }
                // System.out.println(JSON.toJSONString(rolePermissionIds));
                if (!rolePermissionSetOld.isEmpty()) {
                    if (rolePermissionMapper.deleteByIds(rolePermissionIds) < 0) {
                        log.info("删除旧权限错误");
                        throw new BusinessException("编辑角色失败");
                    }
                }
                isUpdate = true;
            }
            if (isUpdate) {
                RoleDO roleDO = new RoleDO();
                roleDO.setId(roleDTO.getId());
                roleDO.setUpdateTime(System.currentTimeMillis() / 1000);
                if (roleMapper.updateRole(roleDO) != 1) {
                    log.warn("更新最后更新时间失败");
                    throw new BusinessException("编辑角色失败");
                }
                transactionManager.commit(transStatus);
            }
        } catch (Exception e) {
            log.warn("编辑角色异常:", e);
            transactionManager.rollback(transStatus);
            throw new BusinessException("编辑角色失败");
        }

        if (!isUpdate) {
            throw new BusinessException("未作出任何修改");
        }
        permissionCacheService.refreshRoleCache(roleDTO.getId());
        return true;
    }

    /**
     * 判断角色是否拥有权限
     * rolePermissionSet是否包含permissionSet
     *
     * @return boolean
     */
    private Boolean checkRolePermission(HashSet<Integer> oldPermissionSet, HashSet<Integer> newPermissionSet) {
        boolean equals = false;
        for (Integer permissionId : newPermissionSet) {
            equals = false;
            for (Integer rolePermissionId : oldPermissionSet) {
                if (rolePermissionId.equals(permissionId)) {
                    equals = true;
                    break;
                }
            }
            if (!equals) {
                break;
            }
        }
        return equals;
    }

    /**
     * 刷新redis缓存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRole(Operator operator, int roleId) {
        RoleDO roleCheck = roleMapper.selectRoleById(roleId);
        if (roleCheck == null) {
            throw new BusinessException("角色不存在");
        }

        if (roleCheck.getRoleLevel() < operator.getRoleLevel()) {
            throw new BusinessException("无权限删除此角色");
        }
        // 角色等级相同，操作角色不是被操作角色创建者时
        if (roleCheck.getRoleLevel().equals(operator.getRoleLevel())
                && !roleCheck.getCreatorId().equals(operator.getRoleId())) {
            throw new BusinessException("无权限删除此角色");
        }
        if (roleMapper.deleteRole(roleId) != 1) {
            throw new BusinessException("删除角色失败");
        }
        rolePermissionMapper.deleteByRoleId(roleId);
        permissionCacheService.deleteRoleCache(roleId);
        return true;
    }

    /**
     * TODO
     * 暂不提供该功能
     */
    @Override
    public Boolean deleteRoles(Operator operator, HashSet<Integer> roleIds) {
        return false;
    }

    @Override
    public Boolean setAccountRole(Operator operator, AccountDTO accountDTO, HashSet<Integer> roleIds) {
        List<String> roleTmp = new ArrayList<>();
        List<AccountRoleDO> accountRoleList = new ArrayList<>();
        AccountRoleDO accountRoleDO;
        List<RoleDO> roleList = roleMapper.selectRoleByIds(roleIds);
        for (RoleDO role : roleList) {
            if (operator.getRoleLevel() > role.getRoleLevel()) {
                roleTmp.add(role.getRoleName());
            }
        }
        if (!roleTmp.isEmpty()) {
            throw new BusinessException("权限不足无法编辑该用户");
        }
        roleList = roleMapper.selectRoleByIds(accountDTO.getRoleIds());
        for (RoleDO role : roleList) {
            if (operator.getRoleLevel() > role.getRoleLevel()) {
                roleTmp.add(role.getRoleName());
            }
            accountRoleDO = new AccountRoleDO();
            accountRoleDO.setAccountId(accountDTO.getId());
            accountRoleDO.setRoleId(role.getId());
            accountRoleList.add(accountRoleDO);
        }
        if (!roleTmp.isEmpty()) {
            throw new BusinessException("不能赋予权限等级高于自己的角色：" + JSON.toJSONString(roleTmp));
        }
        // 获取所有操作角色的权限的并集
        HashSet<Integer> permissionList = rolePermissionMapper.selectPermissionIdByRoleIds(roleIds);
        // 判断操作人员是否拥有所操作权限的权限
        HashSet<Integer> operatorPermissionList = rolePermissionMapper.selectPermissionIdByRoleIds(operator.getRoleIds());
        if (!checkRolePermission(operatorPermissionList, permissionList)) {
            throw new BusinessException("角色未拥有所操作角色所包含的权限");
        }
        accountRoleMapper.deleteAccountRolesByAccountId(accountDTO.getId());
        if (accountRoleMapper.insertAccountRoles(accountRoleList) != accountRoleList.size()) {
            throw new BusinessException("赋予角色失败");
        }
        return true;
    }

    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(transDefinition);
    }

    private List<RolePermissionDO> getRolePermissionDOList(RoleDTO roleDTO) {
        List<RolePermissionDO> rolePermissions = new ArrayList<>();
        for (Integer permissionId : roleDTO.getPermissionSet()) {
            RolePermissionDO permissionDO = new RolePermissionDO();
            permissionDO.setRoleId(roleDTO.getId());
            permissionDO.setPermissionId(permissionId);
            rolePermissions.add(permissionDO);
        }
        return rolePermissions;
    }
}
