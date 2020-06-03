package club.dbg.cms.admin.service.permission;

import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.domain.admin.ServiceDO;
import club.dbg.cms.admin.service.permission.pojo.PermissionListDTO;
import club.dbg.cms.admin.service.permission.pojo.PermissionTree;
import club.dbg.cms.admin.service.permission.pojo.ServiceListDTO;

import java.util.List;

public interface PermissionService {
    ServiceListDTO getServiceList(String serviceName, Integer page, Integer pageSize);

    Boolean addService(ServiceDO service);

    Boolean editService(ServiceDO service);

    Boolean deleteService(Integer id);

    PermissionListDTO getPermissionList(Integer serviceId, String permissionName,
                                        Integer status, Integer page, Integer pageSize);

    List<PermissionTree> getPermissionTree();

    Boolean addPermission(PermissionDO permission);

    Boolean editPermission(PermissionDO permission);

    Boolean deletePermission(Integer id);

    Boolean updatePermission(PermissionDO permission);
}
