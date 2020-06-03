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
    @Value("${login.timeout}")
    private Integer loginTimeOut;

    @Value("${login.redisHeader}")
    private String loginRedisHeader;

    private final RedisUtils redisUtils;

    public LoginCacheServiceImpl(RedisUtils redisUtils) {
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
