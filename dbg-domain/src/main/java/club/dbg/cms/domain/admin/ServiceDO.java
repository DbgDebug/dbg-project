package club.dbg.cms.domain.admin;

import java.util.Date;

/**
 * @author dbg
 */
public class ServiceDO {
    private Integer id;
    private String serviceName;
    private String displayName;
    private Long createTime;
    private Long updateTime;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "ServiceDO{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                '}';
    }
}
