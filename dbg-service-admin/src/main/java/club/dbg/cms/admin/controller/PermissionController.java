package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.service.permission.pojo.PermissionListDTO;
import club.dbg.cms.admin.service.permission.pojo.PermissionTree;
import club.dbg.cms.domain.admin.PermissionDO;
import club.dbg.cms.domain.admin.ServiceDO;
import club.dbg.cms.admin.config.ConfigConsts;
import club.dbg.cms.admin.service.permission.PermissionService;
import club.dbg.cms.admin.service.permission.pojo.ServiceListDTO;
import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import club.dbg.cms.util.ResponseBuild;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dbg
 */
@Validated
@RestController
public class PermissionController {
    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @RequestMapping(value = "get_service_list", name = "获取服务列表", method = RequestMethod.GET)
    public ResponseBuild<ServiceListDTO> getServiceList(
            @Length(max = ConfigConsts.QUERY_SERVICE_NAME_MAX,
                    message = ConfigConsts.QUERY_SERVICE_NAME_DESCRIBE)
            @RequestParam("serviceName") String serviceName,
            @Range(min = ConfigConsts.PAGE_MIN,
                    max = ConfigConsts.PAGE_MAX,
                    message = ConfigConsts.PAGE_DESCRIBE)
            @RequestParam("page") int page,
            @Range(min = ConfigConsts.PAGE_SIZE_MIN,
                    max = ConfigConsts.PAGE_SIZE_MAX,
                    message = ConfigConsts.PAGE_SIZE_DESCRIBE)
            @RequestParam("pageSize") int pageSize) {
        return ResponseBuild.ok(permissionService.getServiceList(serviceName, page, pageSize));
    }

    @RequestMapping(value = "add_service", name = "添加服务", method = RequestMethod.POST)
    public ResponseBuild<Boolean> addService(
            @Length(min = ConfigConsts.SERVICE_NAME_MIN,
                    max = ConfigConsts.SERVICE_NAME_MAX,
                    message = ConfigConsts.SERVICE_NAME_DESCRIBE)
            @RequestParam("serviceName") String serviceName,
            @Length(min = ConfigConsts.SERVICE_DISPLAY_NAME_MIN,
                    max = ConfigConsts.SERVICE_DISPLAY_NAME_MAX,
                    message = ConfigConsts.SERVICE_DISPLAY_NAME_DESCRIBE)
            @RequestParam("displayName") String displayName,
            @Range(min = ConfigConsts.DATA_STATUS_MIN,
                    max = ConfigConsts.DATA_STATUS_MAX,
                    message = ConfigConsts.DATA_STATUS_DESCRIBE)
            @RequestParam("status") int status) {
        ServiceDO serviceData = new ServiceDO();
        serviceData.setServiceName(serviceName);
        serviceData.setDisplayName(displayName);
        serviceData.setStatus(status);
        return ResponseBuild.ok(permissionService.addService(serviceData));
    }

    @RequestMapping(value = "edit_service", name = "编辑服务", method = RequestMethod.POST)
    public ResponseBuild<Boolean> editService(
            @Range(max = ConfigConsts.ID_MAX,
                    message = ConfigConsts.ID_DESCRIBE)
            @RequestParam("id") Integer id,
            @Length(min = ConfigConsts.SERVICE_NAME_MIN,
                    max = ConfigConsts.SERVICE_NAME_MAX,
                    message = ConfigConsts.SERVICE_NAME_DESCRIBE)
            @RequestParam("serviceName") String serviceName,
            @Length(min = ConfigConsts.SERVICE_DISPLAY_NAME_MIN,
                    max = ConfigConsts.SERVICE_DISPLAY_NAME_MAX,
                    message = ConfigConsts.SERVICE_DISPLAY_NAME_DESCRIBE)
            @RequestParam("displayName") String displayName,
            @Range(min = ConfigConsts.DATA_STATUS_MIN,
                    max = ConfigConsts.DATA_STATUS_MAX,
                    message = ConfigConsts.DATA_STATUS_DESCRIBE)
            @RequestParam("status") Integer status) {
        ServiceDO serviceData = new ServiceDO();
        serviceData.setId(id);
        serviceData.setServiceName(serviceName);
        serviceData.setDisplayName(displayName);
        serviceData.setStatus(status);
        return ResponseBuild.ok(permissionService.editService(serviceData));
    }

    @RequestMapping(value = "delete_service", name = "删除服务", method = RequestMethod.POST)
    public ResponseBuild<Boolean> deleteService(@RequestParam("id") int id) {
        return ResponseBuild.ok(permissionService.deleteService(id));
    }

    @RequestMapping(value = "get_permission_list", name = "获取权限列表", method = RequestMethod.GET)
    public ResponseBuild<PermissionListDTO> getPermissionList(
            @Range(min = ConfigConsts.QUERY_SERVICE_ID_MIN,
                    max = ConfigConsts.QUERY_SERVICE_ID_MAX,
                    message = ConfigConsts.QUERY_SERVICE_ID_DESCRIBE)
            @RequestParam(value = "serviceId", required = false, defaultValue = "-1") Integer serviceId,
            @Length(max = ConfigConsts.QUERY_PERMISSION_NAME_MAX,
                    message = ConfigConsts.QUERY_PERMISSION_NAME_DESCRIBE)
            @RequestParam(value = "permissionName", required = false) String permissionName,
            @Range(min = ConfigConsts.QUERY_STATUS_MIN,
                    max = ConfigConsts.QUERY_STATUS_MAX,
                    message = ConfigConsts.QUERY_STATUS_DESCRIBE)
            @RequestParam(value = "status", required = false, defaultValue = "-1") Integer status,
            @Range(min = ConfigConsts.PAGE_MIN,
                    max = ConfigConsts.PAGE_MAX,
                    message = ConfigConsts.PAGE_DESCRIBE)
            @RequestParam("page") int page,
            @Range(min = ConfigConsts.PAGE_SIZE_MIN,
                    max = ConfigConsts.PAGE_SIZE_MAX,
                    message = ConfigConsts.PAGE_SIZE_DESCRIBE)
            @RequestParam("pageSize") int pageSize) {
        return ResponseBuild.ok(permissionService.getPermissionList(serviceId, permissionName, status, page, pageSize));
    }

    @RequestMapping(value = "get_permission_tree", name = "获取权限列表（树形结构）", method = RequestMethod.GET)
    public ResponseBuild<List<PermissionTree>> getPermissionTree() {
        return ResponseBuild.ok(permissionService.getPermissionTree());
    }

    @RequestMapping(value = "add_permission", name = "添加权限", method = RequestMethod.POST)
    public ResponseBuild<Boolean> addPermission(
            @Range(min = ConfigConsts.DATA_SERVICE_ID_MIN,
                    max = ConfigConsts.DATA_SERVICE_ID_MAX,
                    message = ConfigConsts.DATA_SERVICE_ID_DESCRIBE)
            @RequestParam("serviceId") Integer serviceId,
            @Length(min = ConfigConsts.PERMISSION_NAME_MIN,
                    max = ConfigConsts.PERMISSION_NAME_MAX,
                    message = ConfigConsts.PERMISSION_NAME_DESCRIBE)
            @RequestParam("permissionName") String permissionName,
            @Length(min = ConfigConsts.PERMISSION_PATH_MIN,
                    max = ConfigConsts.PERMISSION_PATH_MAX,
                    message = ConfigConsts.PERMISSION_PATH_DESCRIBE)
            @RequestParam("path") String path,
            @Range(min = ConfigConsts.DATA_STATUS_MIN,
                    max = ConfigConsts.DATA_STATUS_MAX,
                    message = ConfigConsts.DATA_STATUS_DESCRIBE)
            @RequestParam("status") Integer status) {
        PermissionDO permission = new PermissionDO();
        permission.setServiceId(serviceId);
        permission.setPermissionName(permissionName);
        permission.setPath(path);
        permission.setStatus(status);
        return ResponseBuild.ok(permissionService.addPermission(permission));
    }

    @RequestMapping(value = "set_permission_status", name = "设置权限状态", method = RequestMethod.POST)
    public ResponseBuild<Boolean> setPermission(
            @Range(max = ConfigConsts.ID_MAX,
                    message = ConfigConsts.ID_DESCRIBE)
            @RequestParam("id") Integer id,
            @Range(max = ConfigConsts.DATA_SERVICE_ID_MAX,
                    message = ConfigConsts.DATA_SERVICE_ID_DESCRIBE)
            @Range(min = ConfigConsts.DATA_STATUS_MIN,
                    max = ConfigConsts.DATA_STATUS_MAX,
                    message = ConfigConsts.DATA_STATUS_DESCRIBE)
            @RequestParam("status") Integer status) {
        PermissionDO permission = new PermissionDO();
        permission.setId(id);
        permission.setStatus(status);
        return ResponseBuild.ok(permissionService.updatePermission(permission));
    }

    @RequestMapping(value = "delete_permission", name = "删除权限", method = RequestMethod.POST)
    public ResponseBuild<Boolean> deletePermission(
            @Range(max = ConfigConsts.ID_MAX, message = ConfigConsts.ID_DESCRIBE)
            @RequestParam("id") int id) {
        return ResponseBuild.ok(permissionService.deletePermission(id));
    }
}
