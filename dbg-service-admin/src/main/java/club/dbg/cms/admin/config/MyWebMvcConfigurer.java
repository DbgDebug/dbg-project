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

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${redis.cache.roleHeader}")
    private String roleHeader;

    @Value("${redis.cache.permissionHeader}")
    private String permissionHeader;

    private final PublicApiConfig publicApiConfig;

    private final RedisUtils redisUtils;


    public MyWebMvcConfigurer(PublicApiConfig publicApiConfig,
                              RedisUtils redisUtils) {
        this.publicApiConfig = publicApiConfig;
        this.redisUtils = redisUtils;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(
                serviceName,
                roleHeader,
                permissionHeader,
                redisUtils,
                publicApiConfig));
    }
}
