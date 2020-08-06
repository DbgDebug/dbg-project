package club.dbg.cms.admin.filter;

import club.dbg.cms.admin.config.PublicApiConfig;
import club.dbg.cms.admin.filter.pojo.MyHttpServletRequest;
import club.dbg.cms.admin.redis.RedisUtils;
import club.dbg.cms.admin.utils.TokenUtils;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import club.dbg.cms.rpc.pojo.TokenDTO;
import club.dbg.cms.util.ResponseCode;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author dbg
 * @date 2019/08/18
 */

@Component
public class AuthFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private final Boolean isDebug;

    private final Integer loginTimeout;

    private final String loginRedisHeader;

    private final PublicApiConfig publicApiConfig;

    private final RedisUtils redisUtils;

    private final TokenUtils requestUtils;

    @Autowired
    public AuthFilter(
            @Value("${system.isDebug}")
                    Boolean isDebug,
            @Value("${login.timeout}")
                    Integer loginTimeout,
            @Value("${login.redisHeader}")
                    String loginRedisHeader,
            PublicApiConfig publicApiConfig,
            RedisUtils redisUtils,
            TokenUtils requestUtils) {
        this.isDebug = isDebug;
        this.loginTimeout = loginTimeout;
        this.loginRedisHeader = loginRedisHeader;
        this.publicApiConfig = publicApiConfig;
        this.redisUtils = redisUtils;
        this.requestUtils = requestUtils;
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * 验证是否登录
     *
     * @param servletRequest  请求
     * @param servletResponse 响应
     * @param filterChain     filterChain
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("{}:{}", request.getMethod(), request.getServletPath());

        MyHttpServletRequest myHttpServletRequest;
        // 公开的接口不进行验证，可以直接访问
        if (publicApiConfig.contains(request.getServletPath())) {
            myHttpServletRequest = new MyHttpServletRequest(request);
            Operator operator = new Operator();
            operator.setIp(this.getIpAddr(request));
            myHttpServletRequest.setOperator(operator);
            filterChain.doFilter(myHttpServletRequest, response);
            return;
        }
        TokenDTO token = requestUtils.getToken(request);
        if (token.getAccessToken() == null || token.getAccessToken().isEmpty()) {
            responseResult(response);
            return;
        }
        Operator operator = requestUtils.getOperatorInfo(token.getAccessToken());
        if (operator == null) {
            responseResult(response);
            return;
        }
        operator.setIp(this.getIpAddr(request));
        myHttpServletRequest = new MyHttpServletRequest(request);
        myHttpServletRequest.setOperator(operator);
        refreshToken(token.getAccessToken(), operator.getId());
        filterChain.doFilter(myHttpServletRequest, response);
    }

    @Override
    public void destroy() {

    }

    private void refreshToken(String accessToken, int userId) {
        redisUtils.expire(loginRedisHeader + accessToken, loginTimeout);
        redisUtils.expire(loginRedisHeader + userId, loginTimeout);
    }

    private void responseResult(HttpServletResponse response) throws IOException {
        ResponseResultDTO serviceResponse = new ResponseResultDTO();
        serviceResponse.setCode(50014);
        serviceResponse.setMessage("Authentication Failed");
        IOUtils.write(JSON.toJSONString(serviceResponse), response.getOutputStream(), Charset.defaultCharset());
    }

    private String getIpAddr(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
