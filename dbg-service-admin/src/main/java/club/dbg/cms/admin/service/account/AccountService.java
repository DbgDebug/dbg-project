package club.dbg.cms.admin.service.account;

import club.dbg.cms.admin.service.account.pojo.AccountDTO;
import club.dbg.cms.admin.service.account.pojo.AccountListDTO;
import club.dbg.cms.rpc.pojo.Operator;

import java.util.List;

/**
 * @author dbg
 */
public interface AccountService {
    AccountListDTO getAccountList(String username, int page, int pageSize);

    AccountDTO getAccountDetail(Integer accountId);
    /**
     * 添加用户
     * @return Boolean
     */
    Boolean addAccount(Operator operator, AccountDTO account);

    Boolean editAccount(Operator operator, AccountDTO account);

    Boolean deleteAccount(Operator operator, Integer id);

    Boolean deleteAccounts(Operator operator, List<Integer> idList);

    Boolean changePassword(int id, String passwordOld, String passwordNew);

    Boolean resetPassword(int id, String code, String passwordNew);

    Boolean resetEmail(int id, String code, String emailNew);
}
