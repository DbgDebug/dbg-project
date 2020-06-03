package club.dbg.cms.admin.service.account.pojo;

import club.dbg.cms.domain.admin.AccountDO;

import java.util.List;

/**
 * @author dbg
 */
public class AccountListDTO {
    private List<AccountDO> accountList;
    private Integer total;

    public List<AccountDO> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountDO> accountList) {
        this.accountList = accountList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "AccountListDTO{" +
                "accountList=" + accountList +
                ", total=" + total +
                '}';
    }
}
