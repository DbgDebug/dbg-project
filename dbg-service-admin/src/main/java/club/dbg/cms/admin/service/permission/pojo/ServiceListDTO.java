package club.dbg.cms.admin.service.permission.pojo;

import club.dbg.cms.domain.admin.ServiceDO;

import java.util.List;

/**
 * @author  dbg
 */
public class ServiceListDTO {
    private List<ServiceDO> serviceList;
    private Integer total;

    public List<ServiceDO> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceDO> serviceList) {
        this.serviceList = serviceList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ServiceListDTO{" +
                "serviceList=" + serviceList +
                ", total=" + total +
                '}';
    }
}
