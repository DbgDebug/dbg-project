package club.dbg.cms.video.filter;


import club.dbg.cms.util.JWTUtils;
import club.dbg.cms.video.config.PublicApiConfig;
import club.dbg.cms.video.config.SaltConfig;
import club.dbg.cms.video.pojo.ExceptionMessage;
import com.alibaba.fastjson.JSON;
import club.dbg.cms.video.pojo.TokenDTO;
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

    @Autowired
    public AuthFilter(
            @Value("${system.isDebug}")
            Boolean isDebug,
            @Value("${session.tokenHeader}")
            String tokenHeader,
            @Value("${session.tokenName}")
            String tokenName) {
        this.isDebug = isDebug;
        this.tokenHeader = tokenHeader;
        this.tokenName = tokenName;
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

        TokenDTO token = getToken(request);
        if (token.getToken() == null || token.getToken().isEmpty()) {
            responseResult(response);
            return;
        }

        JWTUtils.verifyToken(token.getToken(), JWTUtils.getClaimByName(token.getToken(), "accountId").asString() + SaltConfig.SALT);
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
