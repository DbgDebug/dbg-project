package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.AccountRoleDO;
import club.dbg.cms.admin.service.role.pojo.AccountRoleDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

/**
 * @author dbg
 */
@Repository
public interface AccountRoleMapper {
    @Select({
            "select id, account_id, role_id from tb_account_role where account_id = #{accountId}"
    })
    List<AccountRoleDO> selectByAccountId(@Param("accountId") Integer accountId);

    @Select({
            "select role_id from tb_account_role where account_id = #{accountId}"
    })
    HashSet<Integer> selectRoleIdByAccountId(@Param("accountId") Integer accountId);

    List<AccountRoleDetail> selectAccountRoleByAccountId(@Param("accountId") Integer accountId);

    List<AccountRoleDetail> selectAccountRoleByRoleId(@Param("roleId") Integer roleId);

    int insertAccountRoles(List<AccountRoleDO> accountRoles);

    int deleteAccountRolesByAccountId(@Param("accountId") Integer accountId);
}
