package club.dbg.cms.domain.admin;

import java.util.Objects;

/**
 * @author dbg
 * hashCode重写，equals只判断permissionId是否相同
 */
public class PermissionDO {
    private Integer id;
    private Integer serviceId;
    private String permissionId;
    private String permissionName;
    private String path;
    private Long createTime;
    private Long updateTime;
    private Integer status;
    private String method;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionDO that = (PermissionDO) o;
        return permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId);
    }

    @Override
    public String toString() {
        return "PermissionDO{" +
                "id=" + id +
                ", serviceId=" + serviceId +
                ", permissionId='" + permissionId + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", path='" + path + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", method" + method +
                '}';
    }
}
