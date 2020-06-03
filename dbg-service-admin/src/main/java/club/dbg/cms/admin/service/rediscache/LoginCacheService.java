package club.dbg.cms.admin.service.rediscache;

import club.dbg.cms.rpc.pojo.Operator;

/**
 * @author dbg
 */
public interface LoginCacheService {
    boolean set(String accessToken, Operator operator);

    boolean remove(Integer accountId);
}
