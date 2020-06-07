package club.dbg.cms.admin.service.rediscache;

import club.dbg.cms.admin.redis.RedisUtils;
import club.dbg.cms.rpc.pojo.Operator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dbg
 */
@Service
public class LoginCacheServiceImpl implements LoginCacheService {
    private final Integer loginTimeOut;

    private final String loginRedisHeader;

    private final RedisUtils redisUtils;

    public LoginCacheServiceImpl(
            @Value("${login.timeout}")
                    Integer loginTimeOut,
            @Value("${login.redisHeader}")
                    String loginRedisHeader,
            RedisUtils redisUtils) {
        this.loginTimeOut = loginTimeOut;
        this.loginRedisHeader = loginRedisHeader;
        this.redisUtils = redisUtils;
    }


    @Override
    public boolean set(String accessToken, Operator operator) {
        remove(operator.getId());
        accessToken = loginRedisHeader + accessToken;
        redisUtils.set(accessToken, operator, loginTimeOut);
        redisUtils.set(loginRedisHeader + operator.getId(), accessToken, loginTimeOut);
        return false;
    }

    @Override
    public boolean remove(Integer accountId) {
        String tokenStr = (String) redisUtils.get(loginRedisHeader + accountId);
        if (tokenStr != null) {
            redisUtils.delete(tokenStr);
            redisUtils.delete(loginRedisHeader + accountId);
        }
        return true;
    }
}
