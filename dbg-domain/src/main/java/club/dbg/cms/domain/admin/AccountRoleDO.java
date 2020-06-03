package club.dbg.cms.domain.admin;

/**
 * @author dbg
 */
public class AccountRoleDO {
    private Integer id;
    private Integer accountId;
    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "AccountRoleDO{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", roleId=" + roleId +
                '}';
    }
}
