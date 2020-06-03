package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.admin.service.permission.pojo.PermissionDetailDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dbg
 */
@Repository
public interface PermissionMapper {

    @Select({
            "select id, permission_id, permission_name, path, method, status",
            "from tb_permission where id = #{id}"
    })
    PermissionDO selectPermissionById(@Param("id") int id);

    @Select({
            "select id, permission_id, permission_name, path, method, status",
            "from tb_permission where permission_name = #{permissionName}"
    })
    PermissionDO selectByPermissionName(@Param("permissionName") String permissionName);
    @Select({
            "select id, permission_id, permission_name, path, method, status",
            "from tb_permission where path = #{path}"
    })
    PermissionDO selectByPath(@Param("path") String path);


    @Select({
            "select id, permission_id, permission_name, path, method, status",
            "from tb_permission where permission_id = #{permissionId}"
    })
    PermissionDO selectPermissionByPid(@Param("permissionId") String permissionId);

    @Select({
            "select id, service_id, permission_id, permission_name, path, method, create_time, update_time, status",
            "from tb_permission order by path"
    })
    List<PermissionDO> selectPermissionAll();

    @Select({
            "select id, service_id, permission_id, permission_name, path, method, create_time, update_time, status",
            "from tb_permission where status = #{status} order by path"
    })
    List<PermissionDO> selectPermissionByStatus(@Param("status") Integer status);

    List<PermissionDetailDTO> selectPermissionList(@Param("serviceId") Integer serviceId,
                                                   @Param("permissionName") String permissionName,
                                                   @Param("status") Integer status,
                                                   @Param("page") Integer page,
                                                   @Param("pageSize") Integer pageSize);

    int selectPermissionCount(@Param("serviceId") Integer serviceId,
                              @Param("permissionName") String permissionName,
                              @Param("status") Integer status);

    List<PermissionDO> selectByPermissionIds(List<PermissionDO> permissions);

    @Select({
            "select id from tb_permission where service_id = #{serviceId}"
    })
    List<Integer> selectPermissionIdByServiceId(@Param("serviceId") Integer serviceId);

    @Select({
            "select id, service_id, permission_id, permission_name, path, method, create_time, update_time, status",
            "from tb_permission where service_id = #{serviceId}"
    })
    List<PermissionDO> selectByServiceId(@Param("serviceId") Integer serviceId);

    @Insert({
            "insert into tb_permission(service_id, permission_id, permission_name,",
            "path, create_time, update_time, status, method)",
            "values(#{serviceId}, #{permissionId}, #{permissionName}, #{path},",
            "#{createTime}, #{updateTime}, #{status}, #{method})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertPermission(PermissionDO permission);

    void insertPermissions(List<PermissionDO> permissions);

    @Update({
            "update tb_permission set update_time = #{updateTime}, status = #{status}",
            "where id = #{id}"
    })
    int updatePermission(PermissionDO permission);

    int updatePermissions(List<PermissionDO> permissions);

    @Update({
            "UPDATE tb_permission SET status = #{status} WHERE service_id = #{serviceId}"
    })
    void updatePermissionByServiceId(@Param("serviceId") Integer serviceId, @Param("status") Integer status);

    @Delete({
            "delete from tb_permission where id = #{id}"
    })
    int deletePermission(@Param("id") int id);

    int deletePermissions(List<PermissionDO> idArray);

    int deletePermissionByServiceId(@Param("serviceId") Integer serviceId);

    List<PermissionDO> selectPermissionByPath(List<PermissionDO> permissions);
}
