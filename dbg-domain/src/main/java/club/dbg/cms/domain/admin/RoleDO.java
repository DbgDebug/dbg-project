package club.dbg.cms.domain.admin;

/**
 * @author dbg
 */
public class RoleDO {
    private Integer id;
    private Integer creatorId;
    private Integer roleLevel;
    private String roleName;
    private Long createTime;
    private Long updateTime;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RoleDO{" +
                "id=" + id +
                ", creatorId=" + creatorId +
                ", roleLevel=" + roleLevel +
                ", roleName='" + roleName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                '}';
    }
}
