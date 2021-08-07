package club.dbg.cms.admin.service.permission;

import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.domain.admin.ServiceDO;
import club.dbg.cms.admin.dao.PermissionMapper;
import club.dbg.cms.admin.dao.RolePermissionMapper;
import club.dbg.cms.admin.dao.ServiceMapper;
import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.rediscache.PermissionCacheService;
import club.dbg.cms.admin.service.permission.pojo.PermissionListDTO;
import club.dbg.cms.admin.service.permission.pojo.PermissionTree;
import club.dbg.cms.admin.service.permission.pojo.ServiceListDTO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dbg
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);

    private final ServiceMapper serviceMapper;

    private final PermissionMapper permissionMapper;

    private final RolePermissionMapper rolePermissionMapper;

    private final PermissionCacheService permissionCacheService;

    private final DataSourceTransactionManager transactionManager;

    public PermissionServiceImpl(PermissionMapper permissionMapper,
                                 ServiceMapper serviceMapper,
                                 RolePermissionMapper rolePermissionMapper,
                                 PermissionCacheService permissionCacheService,
                                 DataSourceTransactionManager transactionManager) {
        this.permissionMapper = permissionMapper;
        this.serviceMapper = serviceMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.permissionCacheService = permissionCacheService;
        this.transactionManager = transactionManager;
    }

    @Override
    public ServiceListDTO getServiceList(String serviceName, Integer page, Integer pageSize) {
        page = (page - 1) * pageSize;
        ServiceListDTO serviceList = new ServiceListDTO();
        serviceList.setServiceList(serviceMapper.selectServiceList("%" + serviceName + "%", page, pageSize));
        serviceList.setTotal(serviceMapper.selectServiceCount("%" + serviceName + "%"));
        return serviceList;
    }

    @Override
    synchronized public Boolean addService(ServiceDO service) {
        service.setCreateTime(System.currentTimeMillis() / 1000);
        service.setUpdateTime(service.getCreateTime());
        if (serviceMapper.selectServiceByName(service.getServiceName()) != null) {
            throw new BusinessException("服务已存在");
        }
        TransactionStatus transactionStatus = this.getTransactionStatus();
        try {
            if (serviceMapper.insertService(service) != 1) {
                throw new BusinessException("添加服务失败");
            }
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.info("添加服务异常：", e);
            transactionManager.rollback(transactionStatus);
            throw e;
        }
        return true;
    }

    @Override
    synchronized public Boolean editService(ServiceDO service) {
        if (serviceMapper.selectServiceById(service.getId()) == null) {
            throw new BusinessException("服务不存在");
        }
        service.setUpdateTime(System.currentTimeMillis() / 1000);
        TransactionStatus transactionStatus = this.getTransactionStatus();
        try {
            if (serviceMapper.updateService(service) != 1) {
                throw new BusinessException("更新服务失败");
            }
            permissionMapper.updatePermissionByServiceId(service.getId(), service.getStatus());
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.info("更新服务异常");
            transactionManager.rollback(transactionStatus);
            throw e;
        }
        return true;
    }

    /**
     * TODO:只有超级管理员才能操作
     *
     * @param id 服务ID
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteService(Integer id) {
        List<Integer> permissionIds = permissionMapper.selectPermissionIdByServiceId(id);
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new BusinessException("服务不存在");
        }
        log.info(JSON.toJSONString(permissionIds));
        permissionMapper.deletePermissionByServiceId(id);
        rolePermissionMapper.deletePermissionByPermissionIds(permissionIds);
        if (serviceMapper.deleteService(id) != 1) {
            throw new BusinessException("删除失败");
        }
        permissionCacheService.refreshPermissionCache();
        return true;
    }

    @Override
    public PermissionListDTO getPermissionList(Integer serviceId,
                                               String permissionName,
                                               Integer status,
                                               Integer page, Integer pageSize) {
        page = (page - 1) * pageSize;
        if(permissionName != null && !permissionName.isEmpty()){
            permissionName = "%" + permissionName + "%";
        }
        PermissionListDTO permissionList = new PermissionListDTO();
        permissionList.setPermissionList(permissionMapper.selectPermissionList(serviceId,
                permissionName,
                status, page, pageSize));
        permissionList.setTotal(permissionMapper.selectPermissionCount(serviceId, permissionName, status));
        return permissionList;
    }

    /**
     * TODO 缓存到redis
     *
     * @return List<PermissionTree>
     */
    @Override
    public List<PermissionTree> getPermissionTree() {
        List<PermissionTree> permissionTreeList = new ArrayList<>();
        List<ServiceDO> serviceList = serviceMapper.selectServiceAll();
        List<PermissionDO> permissionList = permissionMapper.selectPermissionAll();
        for (ServiceDO serviceDO : serviceList) {
            PermissionTree permissionTree = new PermissionTree();
            permissionTree.setId(serviceDO.getId() * -1);
            permissionTree.setName(serviceDO.getDisplayName());
            permissionTree.setDisabled(false);
            List<PermissionTree> permissionTrees = new ArrayList<>();
            for (PermissionDO permissionDO : permissionList) {
                if (permissionDO.getStatus() != 1) {
                    continue;
                }
                if (permissionDO.getServiceId().equals(serviceDO.getId())) {
                    PermissionTree p = new PermissionTree();
                    p.setId(permissionDO.getId());
                    p.setName(permissionDO.getPermissionName());
                    p.setDisabled(false);
                    permissionTrees.add(p);
                }
            }
            permissionTree.setChildren(permissionTrees);
            permissionTreeList.add(permissionTree);
        }
        return permissionTreeList;
    }

    /**
     * 添加权限时，将权限赋予超级管理员
     *
     * @param permission 权限信息
     * @return boolean
     */
    @Override
    synchronized public Boolean addPermission(PermissionDO permission) {
        return true;
    }

    /**
     * 修改完成后需更新redis的缓存信息
     *
     * @param permission 权限信息
     * @return boolean
     */
    @Override
    synchronized public Boolean editPermission(PermissionDO permission) {
        return true;
    }

    /**
     * 删除完成后更新redis里的角色数据
     *
     * @param id id
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePermission(Integer id) {
        if (permissionMapper.deletePermission(id) != 1) {
            throw new BusinessException("删除失败");
        }
        rolePermissionMapper.deleteByPermissionId(id);
        permissionCacheService.refreshPermissionCache();
        return true;
    }

    @Override
    public Boolean updatePermission(PermissionDO permission) {
        permission.setUpdateTime(System.currentTimeMillis() / 1000);
        if (permissionMapper.updatePermission(permission) != 1) {
            throw new BusinessException("修改失败");
        }

        return true;
    }

    private boolean serviceVerification(int serviceId) {
        return serviceMapper.selectServiceById(serviceId) == null;
    }

    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(transDefinition);
    }
}
