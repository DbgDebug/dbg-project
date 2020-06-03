package club.dbg.cms.admin.service.account.pojo;

import java.util.Date;
import java.util.HashSet;

/**
 * @author dbg
 */
public class AccountDTO {
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private String email;
    private Long createTime;
    private Long updateTime;
    private Long registerTime;
    private Long lastTime;
    private String lastIp;
    private Integer sex;
    private Integer status;
    private HashSet<Integer> roleIds;
    private HashSet<Integer> roleLevels;
    private HashSet<Integer> permissionSet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public HashSet<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(HashSet<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public HashSet<Integer> getRoleLevels() {
        return roleLevels;
    }

    public void setRoleLevels(HashSet<Integer> roleLevels) {
        this.roleLevels = roleLevels;
    }

    public HashSet<Integer> getPermissionSet() {
        return permissionSet;
    }

    public void setPermissionSet(HashSet<Integer> permissionSet) {
        this.permissionSet = permissionSet;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", registerTime=" + registerTime +
                ", lastTime=" + lastTime +
                ", lastIp='" + lastIp + '\'' +
                ", sex=" + sex +
                ", status=" + status +
                ", roleIds=" + roleIds +
                ", roleLevels=" + roleLevels +
                ", permissionSet=" + permissionSet +
                '}';
    }
}
