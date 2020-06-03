package club.dbg.cms.admin.service.permission.pojo;

import java.util.Date;

/**
 * @author dbg
 */
public class PermissionDetailDTO {
    private Integer id;
    private Integer serviceId;
    private String serviceName;
    private String displayName;
    private String permissionId;
    private String permissionName;
    private String path;
    private Long createTime;
    private Long updateTime;
    private Integer status;
    private Integer isUpdate;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "PermissionDetailDTO{" +
                "id=" + id +
                ", serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", permissionId='" + permissionId + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", path='" + path + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", isUpdate=" + isUpdate +
                ", method='" + method + '\'' +
                '}';
    }
}
