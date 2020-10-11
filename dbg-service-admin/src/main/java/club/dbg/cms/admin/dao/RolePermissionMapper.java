package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.RolePermissionDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

/**
 * @author dbg
 */
@Repository
public interface RolePermissionMapper {
    List<RolePermissionDO> selectByRoleId(@Param("roleId") Integer roleId);

    HashSet<Integer> selectPermissionIdByRoleId(@Param("roleId") Integer roleId);

    HashSet<Integer> selectPermissionIdByRoleIds(@Param("roleIds") HashSet<Integer> roleIds);

    int insertRolePermission(RolePermissionDO rolePermissionDO);

    int insertRolePermissions(@Param("rolePermissionList") List<RolePermissionDO> rolePermissionList);

    int deleteByRoleId(@Param("roleId") Integer roleId);

    int deleteByPermissionId(@Param("permissionId") Integer permissionId);

    int deletePermissionByPermissionIds(List<Integer> permissionIds);

    int deleteByIds(@Param("ids") HashSet<Integer> ids);
}
