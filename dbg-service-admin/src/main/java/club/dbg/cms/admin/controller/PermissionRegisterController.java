package club.dbg.cms.admin.controller;

import club.dbg.cms.rpc.PermissionRegisterService;
import club.dbg.cms.rpc.pojo.PermissionRegisterDTO;
import club.dbg.cms.util.ResponseBuild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rpc/permission")
public class PermissionRegisterController {
    private static final Logger log = LoggerFactory.getLogger(PermissionRegisterController.class);

    private final PermissionRegisterService permissionRegisterService;

    public PermissionRegisterController(PermissionRegisterService permissionRegisterService) {
        this.permissionRegisterService = permissionRegisterService;
    }

    @RequestMapping(value = "/register")
    public ResponseEntity<ResponseBuild<Boolean>> permissionRegister(@RequestBody PermissionRegisterDTO permissionRegisterDTO) {
        return ResponseBuild.build(permissionRegisterService.permissionRegister(permissionRegisterDTO));
    }
}
