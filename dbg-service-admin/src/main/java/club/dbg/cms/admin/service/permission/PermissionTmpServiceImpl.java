package club.dbg.cms.admin.service.permission;

import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.domain.admin.RolePermissionDO;
import club.dbg.cms.admin.config.RoleConfig;
import club.dbg.cms.admin.dao.PermissionMapper;
import club.dbg.cms.admin.dao.PermissionTmpMapper;
import club.dbg.cms.admin.dao.RolePermissionMapper;
import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.permission.pojo.PermissionTmpListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author dbg
 */
@Service
public class PermissionTmpServiceImpl implements PermissionTmpService {
    private static final Logger log = LoggerFactory.getLogger(PermissionTmpServiceImpl.class);

    private final PermissionTmpMapper permissionTmpMapper;

    private final PermissionMapper permissionMapper;

    private final RolePermissionMapper rolePermissionMapper;

    private final RoleConfig permissionConfig;

    private final DataSourceTransactionManager transactionManager;

    public PermissionTmpServiceImpl(PermissionTmpMapper permissionTmpMapper,
                                    PermissionMapper permissionMapper,
                                    RoleConfig permissionConfig,
                                    RolePermissionMapper rolePermissionMapper,
                                    DataSourceTransactionManager transactionManager) {
        this.permissionTmpMapper = permissionTmpMapper;
        this.permissionMapper = permissionMapper;
        this.permissionConfig = permissionConfig;
        this.rolePermissionMapper = rolePermissionMapper;
        this.transactionManager = transactionManager;
    }

    @Override
    public PermissionTmpListDTO getPermissionTmpList(Integer serviceId,
                                                     Integer status,
                                                     String permissionName,
                                                     Integer page, Integer pageSize) {
        page = (page - 1) * pageSize;
        PermissionTmpListDTO permissionTmpList = new PermissionTmpListDTO();
        permissionTmpList.setPermissionList(
                permissionTmpMapper.selectPermissionList(serviceId, status,
                        permissionName, page, pageSize));

        permissionTmpList.setTotal(permissionTmpMapper.selectPermissionCount(serviceId,
                status, permissionName));
        return permissionTmpList;
    }

    @Override
    synchronized public Boolean addToPermission(Integer id) {
        PermissionDO permissionTmp = permissionTmpMapper.selectPermissionById(id);
        if (permissionTmp == null) {
            throw new BusinessException("所选权限不存在");
        }
        PermissionDO permission = permissionMapper.selectPermissionByPid(permissionTmp.getPermissionId());
        if (permission != null) {
            throw new BusinessException("权限已存在，不需要添加");
        }
        permissionTmp.setCreateTime(System.currentTimeMillis() / 1000);
        permissionTmp.setUpdateTime(permissionTmp.getCreateTime());
        TransactionStatus transactionStatus = this.getTransactionStatus();
        try {
            if (permissionMapper.insertPermission(permissionTmp) != 1) {
                log.info("添加权限表失败");
                throw new BusinessException("添加失败");
            }
            int permissionStatus = 0;
            if (permissionTmpMapper.setPermissionStatus(id, permissionStatus) != 1) {
                log.info("更新临时权限表状态失败");
                throw new BusinessException("添加失败");
            }
            RolePermissionDO rolePermissionDO = new RolePermissionDO();
            rolePermissionDO.setRoleId(permissionConfig.getSuperAdministratorId());
            rolePermissionDO.setPermissionId(permissionTmp.getId());
            if (rolePermissionMapper.insertRolePermission(rolePermissionDO) != 1) {
                log.info("更新超级管理员权限失败");
                throw new BusinessException("添加失败");
            }
            transactionManager.commit(transactionStatus);
        } catch (Exception e){
            log.info("添加权限失败:", e);
            transactionManager.rollback(transactionStatus);
            throw new BusinessException("添加失败");
        }
        return true;
    }

    @Override
    synchronized public Boolean updateToPermission(Integer id) {
        PermissionDO permissionTmp = permissionTmpMapper.selectPermissionById(id);
        if (permissionTmp == null) {
            throw new BusinessException("所选权限不存在");
        }
        PermissionDO permission = permissionMapper.selectPermissionByPid(permissionTmp.getPermissionId());
        if (permission == null) {
            throw new BusinessException("更新的权限不存在");
        }
        if (permissionTmp.getPermissionName().equals(permission.getPermissionName())
                && permissionTmp.getPath().equals(permission.getPath())) {
            throw new BusinessException("不需要更新");
        }
        permission.setPermissionName(permissionTmp.getPermissionName());
        permission.setPath(permissionTmp.getPath());
        permission.setUpdateTime(System.currentTimeMillis() / 1000);
        TransactionStatus transactionStatus = this.getTransactionStatus();
        try{
            if (permissionMapper.updatePermission(permission) != 1) {
                throw new BusinessException("更新失败");
            }
            int permissionStatus = 0;
            if (permissionTmpMapper.setPermissionStatus(id, permissionStatus) != 1) {
                throw new BusinessException("更新失败");
            }
            transactionManager.commit(transactionStatus);
        }catch (Exception e){
            log.info("更新权限失败:", e);
            transactionManager.rollback(transactionStatus);
            throw e;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePermission(Integer id) {
        if (permissionTmpMapper.deletePermission(id) != 1) {
            throw new BusinessException("删除失败");
        }
        return true;
    }

    @Override
    public Boolean setStatus(Integer id, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException("设置错误");
        }
        if (permissionTmpMapper.setPermissionStatus(id, status) != 1) {
            throw new BusinessException("设置失败");
        }
        return true;
    }

    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionManager.getTransaction(transDefinition);
    }
}
