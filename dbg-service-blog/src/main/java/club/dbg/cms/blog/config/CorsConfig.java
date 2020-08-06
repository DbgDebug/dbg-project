package club.dbg.cms.blog.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig {
    private final List<String> allowedOriginList = new ArrayList<>();

    public List<String> getAllowedOriginList() {
        return allowedOriginList;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        for (String item : allowedOriginList) {
            config.addAllowedOrigin(item);
        }

        // 允许所有访问
        config.addAllowedOrigin("*");
        // 限制 HEADER 或 METHOD
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));

        //设置在最前
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

