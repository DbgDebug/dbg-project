package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.RoleDO;
import club.dbg.cms.rpc.pojo.RoleDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

/**
 * @author dbg
 */
@Repository
public interface RoleMapper {
    /**
     * 获取角色列表
     * @param roleName
     * @param page
     * @param pageSize
     * @return
     */
    List<RoleDTO> selectRoleList(@Param("roleName") String roleName,
                                 @Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 获取查询角色总数
     * @param roleName
     * @return
     */
    Integer selectRoleCount(@Param("roleName") String roleName);

    @Select({
            "select id, creator_id, role_name, role_level, create_time, update_time, status ",
            "from tb_role"
    })
    List<RoleDO> selectRoleAll();

    RoleDTO selectRoleDTOById(@Param("id") Integer id);

    RoleDO selectRoleById(@Param("id") Integer id);

    List<RoleDO> selectRoleByIds(@Param("roleIds") HashSet<Integer> ids);

    RoleDO selectRoleByRoleName(@Param("roleName") String roleName);

    int insertRole(RoleDO roleDO);

    int updateRole(RoleDO roleDO);

    List<RoleDO> selectRoleLevelByIds(List<Integer> ids);

    int deleteRole(@Param("id") Integer id);
}
