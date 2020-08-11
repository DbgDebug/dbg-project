package club.dbg.cms.video.filter;


import club.dbg.cms.video.config.PublicApiConfig;
import club.dbg.cms.video.pojo.ExceptionMessage;
import club.dbg.cms.video.redis.RedisUtils;
import com.alibaba.fastjson.JSON;
import club.dbg.cms.video.pojo.DeviceSession;
import club.dbg.cms.video.pojo.TokenDTO;
import club.dbg.cms.video.pojo.UserSession;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
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

    private final String tokenHeader;

    private final String tokenName;

    private final Integer loginTimeout;

    private final RedisUtils redisUtils;

    @Autowired
    public AuthFilter(
            @Value("${system.isDebug}")
            Boolean isDebug,
            @Value("${session.tokenHeader}")
            String tokenHeader,
            @Value("${session.tokenName}")
            String tokenName,
            @Value("${login.timeout}")
            Integer loginTimeout,
            RedisUtils redisUtils) {
        this.isDebug = isDebug;
        this.tokenHeader = tokenHeader;
        this.tokenName = tokenName;
        this.loginTimeout = loginTimeout;
        this.redisUtils = redisUtils;
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("{}:{}", request.getMethod(), request.getServletPath());

        if (isDebug) {
            filterChain.doFilter(request, response);
            return;
        }

        MyHttpServletRequest myRequest;
        if (PublicApiConfig.apiSet.contains(request.getServletPath())) {
            myRequest = new MyHttpServletRequest(request);
            filterChain.doFilter(myRequest, response);
            return;
        }

        if (request.getServletPath().indexOf("/WebSocket") == 0) {
            myRequest = new MyHttpServletRequest(request);
            filterChain.doFilter(myRequest, response);
            return;
        }

        if (request.getServletPath().indexOf("/device/video") == 0) {
            myRequest = new MyHttpServletRequest(request);
            filterChain.doFilter(myRequest, response);
            return;
        }

        if (request.getServletPath().indexOf("/user/video") == 0) {
            myRequest = new MyHttpServletRequest(request);
            filterChain.doFilter(myRequest, response);
            return;
        }

        TokenDTO token = getToken(request);
        if (token.getToken() == null || token.getToken().isEmpty()) {
            responseResult(response);
            return;
        }

        if (request.getServletPath().indexOf("/device") == 0) {
            log.info("device");
            DeviceSession deviceSession = getDeviceSession(token);
            if (deviceSession == null) {
                responseResult(response);
                return;
            }
            refreshDeviceToken(token.getToken(), deviceSession.getId());
            myRequest = new MyHttpServletRequest(request);
            myRequest.setId(deviceSession.getId());
            myRequest.setToken(token.getToken());
            filterChain.doFilter(myRequest, response);
        } else if (request.getServletPath().indexOf("/user") == 0) {
            UserSession userSession = getUserSession(token);
            if (userSession == null) {
                responseResult(response);
                return;
            }
            refreshUserToken(token.getToken(), userSession.getId());
            myRequest = new MyHttpServletRequest(request);
            myRequest.setId(userSession.getId());
            myRequest.setToken(token.getToken());
            filterChain.doFilter(myRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private DeviceSession getDeviceSession(TokenDTO tokenDTO) {
        return (DeviceSession) redisUtils.get("DEVICE-" + tokenDTO.getToken());
    }

    private UserSession getUserSession(TokenDTO tokenDTO) {
        return (UserSession) redisUtils.get("USER-" + tokenDTO.getToken());
    }

    @Override
    public void destroy() {

    }

    private TokenDTO getToken(HttpServletRequest request) {
        TokenDTO token = new TokenDTO();
        String tokenStr = request.getHeader(tokenHeader);
        if (tokenStr != null && !tokenStr.isEmpty()) {
            token.setToken(tokenStr);
            return token;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return token;
        }
        for (Cookie cookie : cookies) {
            if (tokenName.equals(cookie.getName())) {
                token.setToken(cookie.getValue());
            }
        }
        return token;
    }

    private void refreshDeviceToken(String accessToken, int userId) {
        redisUtils.expire("DEVICE-" + accessToken, loginTimeout);
        redisUtils.expire("DEVICE_ID-" + userId, loginTimeout);
    }

    private void refreshUserToken(String accessToken, int userId) {
        redisUtils.expire("USER-" + accessToken, loginTimeout);
        redisUtils.expire("USER_ID-" + userId, loginTimeout);
    }

    private void responseResult(HttpServletResponse response) throws IOException {
        ExceptionMessage message = new ExceptionMessage();
        message.setCode(40000);
        message.setMessage("Authentication Failed");
        IOUtils.write(JSON.toJSONString(message), response.getOutputStream(), Charset.defaultCharset());
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
