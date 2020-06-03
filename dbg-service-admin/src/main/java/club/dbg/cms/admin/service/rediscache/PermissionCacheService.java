package club.dbg.cms.admin.service.rediscache;

/**
 * @author dbg
 */
public interface PermissionCacheService {
    void init();

    Boolean refreshRoleCache(Integer roleId);

    Boolean deleteRoleCache(Integer roleId);

    Boolean refreshPermissionCache();
}
