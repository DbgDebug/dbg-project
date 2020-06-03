package club.dbg.cms.admin.service.permission.pojo;

import java.util.List;

/**
 * @author dbg
 */
public class PermissionTmpListDTO {
    private List<PermissionDetailDTO> permissionList;
    private Integer total;

    public List<PermissionDetailDTO> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<PermissionDetailDTO> permissionList) {
        this.permissionList = permissionList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PermissionTmpListDTO{" +
                "permissionList=" + permissionList +
                ", total=" + total +
                '}';
    }
}
