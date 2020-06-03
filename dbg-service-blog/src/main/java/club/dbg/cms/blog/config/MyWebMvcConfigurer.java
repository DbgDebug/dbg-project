package club.dbg.cms.blog.config;

import club.dbg.cms.blog.interceptor.AuthInterceptor;
import club.dbg.cms.blog.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Value("${system.isDebug}")
    public Boolean isDebug;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${redis.cache.roleHeader}")
    private String roleHeader;

    @Value("${redis.cache.permissionHeader}")
    private String permissionHeader;

    private final RedisUtils redisUtils;


    public MyWebMvcConfigurer(RedisUtils redisUtils) {
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
