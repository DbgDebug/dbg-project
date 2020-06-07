package club.dbg.cms.admin.utils;

import club.dbg.cms.admin.redis.RedisUtils;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.TokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component
public class TokenUtils {
    private final String tokenHeader;

    private final String loginRedisHeader;

    private final RedisUtils redisUtils;

    public TokenUtils(@Value("${session.tokenHeader}")
                              String tokenHeader,
                      @Value("${login.redisHeader}")
                              String loginRedisHeader,
                      RedisUtils redisUtils) {
        this.tokenHeader = tokenHeader;
        this.loginRedisHeader = loginRedisHeader;
        this.redisUtils = redisUtils;
    }

    public TokenDTO getToken(HttpServletRequest request) {
        TokenDTO token = new TokenDTO();
        String tokenStr = request.getHeader(tokenHeader);
        if (tokenStr == null || tokenStr.isEmpty()) {
            return token;
        }
        token.setAccessToken(tokenStr);
        return token;
    }

    public Operator getOperatorInfo(String accessToken) {
        return (Operator) redisUtils.get(loginRedisHeader + accessToken);
    }

}
