package club.dbg.cms.admin.config;

import club.dbg.cms.admin.interceptor.AuthInterceptor;
import club.dbg.cms.admin.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    private final String serviceName;

    private final String roleHeader;

    private final String permissionHeader;

    private final PublicApiConfig publicApiConfig;

    private final RedisUtils redisUtils;

    private final Boolean isDebug;


    public MyWebMvcConfigurer(
            @Value("${system.isDebug}")
                    Boolean isDebug,
            @Value("${spring.application.name}")
                    String serviceName,
            @Value("${redis.cache.roleHeader}")
                    String roleHeader,
            @Value("${redis.cache.permissionHeader}")
                    String permissionHeader,
            PublicApiConfig publicApiConfig,
            RedisUtils redisUtils) {
        this.isDebug = isDebug;
        this.serviceName = serviceName;
        this.roleHeader = roleHeader;
        this.permissionHeader = permissionHeader;
        this.publicApiConfig = publicApiConfig;
        this.redisUtils = redisUtils;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(
                isDebug,
                serviceName,
                roleHeader,
                permissionHeader,
                redisUtils,
                publicApiConfig));
    }
}
