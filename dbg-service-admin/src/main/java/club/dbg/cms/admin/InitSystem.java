package club.dbg.cms.admin;

import club.dbg.cms.admin.service.rediscache.PermissionCacheService;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author dbg
 */
@Component
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
        permissionCache();
    }

    private void permissionCache() {
        permissionCacheService.init();
    }
}
