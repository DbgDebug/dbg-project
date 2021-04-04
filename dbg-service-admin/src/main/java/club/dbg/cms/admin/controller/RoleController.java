package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.config.ConfigConsts;
import club.dbg.cms.admin.filter.pojo.MyHttpServletRequest;
import club.dbg.cms.admin.service.role.RoleService;
import club.dbg.cms.admin.service.role.pojo.RoleListDTO;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import club.dbg.cms.rpc.pojo.RoleDTO;
import club.dbg.cms.util.ResponseBuild;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.HashSet;

/**
 * @author dbg
 */
@Validated
@RestController
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(value = "get_role_list", name = "获取角色列表", method = RequestMethod.GET)
    public ResponseBuild<RoleListDTO> getRoleList(
            @Pattern(regexp = "^$|[^0-9][\\u4E00-\\u9FA5A-Za-z0-9_]{0,16}$", message = "不能为空")
            @RequestParam(value = "roleName", required = false) String roleName,
            @Range(min = ConfigConsts.PAGE_MIN, max = ConfigConsts.PAGE_MAX, message = ConfigConsts.PAGE_DESCRIBE)
            @RequestParam("page") Integer page,
            @Range(min = ConfigConsts.PAGE_SIZE_MIN,
                    max = ConfigConsts.PAGE_SIZE_MAX,
                    message = ConfigConsts.PAGE_SIZE_DESCRIBE)
            @RequestParam("pageSize") Integer pageSize) {
        return ResponseBuild.ok(roleService.getRoleList(roleName, page, pageSize));
    }

    @RequestMapping(value = "add_role", name = "添加角色", method = RequestMethod.POST)
    public ResponseBuild<Boolean> addRole(
            MyHttpServletRequest request,
            @Range(min = 1, max = 100000, message = "")
            @RequestParam("roleLevel") Integer roleLevel,
            @Pattern(regexp = "^$|[^0-9][\\u4E00-\\u9FA5A-Za-z0-9_]{0,16}$", message = "")
            @RequestParam("roleName") String roleName,
            @RequestParam("status") Integer status,
            @RequestParam("permissionSet") HashSet<Integer> permissionSet) {
        Operator operator = request.getOperator();
        RoleDTO role = new RoleDTO();
        role.setRoleLevel(roleLevel);
        role.setRoleName(roleName);
        role.setStatus(status);
        HashSet<Integer> tmpSet = new HashSet<>();
        for (Integer pid : permissionSet) {
            if (pid > 0) {
                tmpSet.add(pid);
            }
        }
        role.setPermissionSet(tmpSet);
        return ResponseBuild.ok(roleService.addRole(operator, role));
    }

    @RequestMapping(value = "edit_role", name = "编辑角色", method = RequestMethod.POST)
    public ResponseBuild<Boolean> editRole(
            MyHttpServletRequest request,
            @RequestParam("id") Integer id,
            @RequestParam("roleLevel") Integer roleLevel,
            @RequestParam("roleName") String roleName,
            @RequestParam("status") Integer status,
            @RequestParam("permissionSet") HashSet<Integer> permissionSet) {
        Operator operator = request.getOperator();
        RoleDTO role = new RoleDTO();
        role.setId(id);
        role.setRoleLevel(roleLevel);
        role.setRoleName(roleName);
        role.setStatus(status);
        HashSet<Integer> tmpSet = new HashSet<>();
        for (Integer pid : permissionSet) {
            if (pid > 0) {
                tmpSet.add(pid);
            }
        }
        role.setPermissionSet(tmpSet);
        return ResponseBuild.ok(roleService.editRole(operator, role));
    }

    @RequestMapping(value = "delete_role", name = "删除角色", method = RequestMethod.POST)
    public ResponseBuild<Boolean> deleteRole(MyHttpServletRequest request,
                                                        @RequestParam("id") Integer id) {
        Operator operator = request.getOperator();
        return ResponseBuild.ok(roleService.deleteRole(operator, id));
    }
}
