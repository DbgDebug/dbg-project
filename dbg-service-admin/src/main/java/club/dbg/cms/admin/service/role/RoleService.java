package club.dbg.cms.admin.service.role;

import club.dbg.cms.admin.service.account.pojo.AccountDTO;
import club.dbg.cms.admin.service.role.pojo.RoleListDTO;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.RoleDTO;

import java.util.HashSet;

/**
 * @author dbg
 */
public interface RoleService {
    /**
     * 获取角色列表
     * @param roleName 角色名
     * @param page 页数
     * @param pageSize 页大小
     * @return RoleListDTO
     */
    RoleListDTO getRoleList(String roleName, int page, int pageSize);

    Boolean addRole(Operator operator, RoleDTO role);

    Boolean editRole(Operator operator, RoleDTO role);

    Boolean deleteRole(Operator operator, int roleId);

    Boolean deleteRoles(Operator operator, HashSet<Integer> roleIds);

    Boolean setAccountRole(Operator operator, AccountDTO accountDTO, HashSet<Integer> roleIds);
}
