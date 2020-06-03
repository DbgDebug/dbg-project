package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.AccountDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dbg
 * @date 2018/08/17
 */

@Repository
public interface AccountMapper {

    List<AccountDO> selectAccountList(@Param("username") String username,
                                      @Param("page") int page,
                                      @Param("pageSize") int pageSize);

    int countAccountList(@Param("username") String username);

    @Select({
            "select id, username, password, real_name, register_time, last_time, ",
            "last_ip, create_time, update_time, email, sex, status",
            "from tb_account where id = #{id} limit 1"
    })
    AccountDO selectAccountById(@Param("id") int id);
    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return Account
     */
    @Select({
            "select id, username, password, register_time, last_time, ",
            "last_ip, create_time, update_time, email, sex, status",
            "from tb_account where username = #{username} limit 1"
    })
    AccountDO selectAccountByUsername(@Param("username") String username);

    @Insert({
            "insert into tb_account(username, password, real_name, register_time, last_time, last_ip,",
            "create_time, update_time, email, sex, status) ",
            "values(#{username}, #{password}, #{realName}, #{registerTime}, #{lastTime}, #{lastIp},",
            "#{createTime}, #{updateTime}, #{email}, #{sex}, #{status})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertAccount(AccountDO account);

    int updateAccount(AccountDO accountDO);

    @Update({
            "update tb_account set last_time = #{lastTime}, last_ip = #{lastIp} ",
            "where id = #{id}"
    })
    int updateLoginInfo(AccountDO account);

    @Update({
            "update tb_account set password = #{password} where id = #{id}"
    })
    int updateAccountPassword(AccountDO account);

    @Update({
            "update tb_account set email = #{email} where id = #{id}"
    })
    int updateAccountEmail(AccountDO account);

    @Delete({
            "delete from tb_account where id = #{id} limit 1"
    })
    int deleteAccount(@Param("id") Integer id);

    int deleteAccounts(int[] idArray);
}
