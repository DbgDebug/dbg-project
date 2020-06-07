package club.dbg.cms.blog.config;

import club.dbg.cms.blog.interceptor.AuthInterceptor;
import club.dbg.cms.blog.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    public final Boolean isDebug;

    private final String serviceName;

    private final String roleHeader;

    private final String permissionHeader;

    private final RedisUtils redisUtils;

    public MyWebMvcConfigurer(
            @Value("${system.isDebug}")
                    Boolean isDebug,
            @Value("${spring.application.name}")
                    String serviceName,
            @Value("${redis.cache.roleHeader}")
                    String roleHeader,
            @Value("${redis.cache.permissionHeader}")
                    String permissionHeader,
            RedisUtils redisUtils) {
        this.isDebug = isDebug;
        this.serviceName = serviceName;
        this.roleHeader = roleHeader;
        this.permissionHeader = permissionHeader;
        this.redisUtils = redisUtils;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(
                serviceName,
                roleHeader,
                permissionHeader,
                redisUtils,
                this));
    }
}
