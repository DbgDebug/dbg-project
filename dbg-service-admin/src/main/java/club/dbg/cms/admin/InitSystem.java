package club.dbg.cms.admin;

import club.dbg.cms.admin.service.rediscache.PermissionCacheService;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

/**
 * @author dbg
 */
@Service
public class InitSystem {
    private final PermissionRegister permissionRegister;

    private final PermissionCacheService permissionCacheService;

    public InitSystem(PermissionRegister permissionRegister, PermissionCacheService permissionCacheService) {
        this.permissionRegister = permissionRegister;
        this.permissionCacheService = permissionCacheService;
    }

    @PostConstruct
    public void init() {
        permissionRegister.init();
        permissionCacheService.init();
    }
}
