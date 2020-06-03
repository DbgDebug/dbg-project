package club.dbg.cms.admin.service.role.pojo;

import club.dbg.cms.rpc.pojo.RoleDTO;

import java.util.List;

/**
 * @author dbg
 */
public class RoleListDTO {
    private List<RoleDTO> roleList;
    private Integer total;

    public List<RoleDTO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleDTO> roleList) {
        this.roleList = roleList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "RoleListDTO{" +
                "roleList=" + roleList +
                ", total=" + total +
                '}';
    }
}
