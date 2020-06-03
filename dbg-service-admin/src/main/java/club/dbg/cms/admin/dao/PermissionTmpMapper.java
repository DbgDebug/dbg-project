package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.admin.service.permission.pojo.PermissionDetailDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionTmpMapper {
    @Select({
            "select id, service_id, permission_id, permission_name, path, status,",
            "create_time, update_time",
            "from tb_permission_tmp where id = #{id}"
    })
    PermissionDO selectPermissionById(@Param("id") int id);

    List<PermissionDetailDTO> selectPermissionList(@Param("serviceId") int serviceId,
                                                   @Param("status") int status,
                                                   @Param("permissionName") String permissionName,
                                                   @Param("page") int page,
                                                   @Param("pageSize") int pageSize);

    int selectPermissionCount(@Param("serviceId") int serviceId,
                              @Param("status") int status,
                              @Param("permissionName") String permissionName);

    List<PermissionDO> selectPermissionId(List<PermissionDO> permissions);

    List<PermissionDO> selectPermissionByPath(List<PermissionDO> permissions);

    int insertPermissions(List<PermissionDO> permissions);

    @Delete({
            "delete from tb_permission_tmp where id = #{id}"
    })
    int deletePermission(@Param("id") int id);

    int deletePermissions(List<PermissionDO> permissions);

    @Update({
            "update tb_permission_tmp set status = #{status} where id = #{id}"
    })
    int setPermissionStatus(@Param("id") int id, @Param("status") int status);
}
