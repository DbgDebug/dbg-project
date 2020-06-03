package club.dbg.cms.rpc.pojo;

import club.dbg.cms.domain.admin.PermissionDO;

import java.util.List;

public class PermissionRegisterDTO {
    private String serviceName;

    private List<PermissionDO> permissionList;

    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<PermissionDO> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<PermissionDO> permissionList) {
        this.permissionList = permissionList;
    }
}
