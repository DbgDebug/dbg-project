package club.dbg.cms.admin.service.permission;

import club.dbg.cms.admin.service.permission.pojo.PermissionTmpListDTO;

public interface PermissionTmpService {
    PermissionTmpListDTO getPermissionTmpList(Integer serviceId,
                                              Integer status,
                                              String permissionName,
                                              Integer page, Integer pageSize);

    Boolean addToPermission(Integer id);

    Boolean updateToPermission(Integer id);

    Boolean deletePermission(Integer id);

    Boolean setStatus(Integer id, Integer status);
}
