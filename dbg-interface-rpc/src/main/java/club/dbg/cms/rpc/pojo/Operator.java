package club.dbg.cms.rpc.pojo;

import java.util.HashSet;

/**
 * @author dbg
 */
public class Operator {
    private Integer id;
    private String username;
    private String accessToken;
    private HashSet<Integer> roleIds;

    private HashSet<Integer> permissionSet;
    private Integer roleId;
    private Integer roleLevel;
    private String ip;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public HashSet<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(HashSet<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public HashSet<Integer> getPermissionSet() {
        return permissionSet;
    }

    public void setPermissionSet(HashSet<Integer> permissionSet) {
        this.permissionSet = permissionSet;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
