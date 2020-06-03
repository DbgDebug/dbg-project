package club.dbg.cms.admin.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域请求处理
 *
 * @author Guoqing
 */
@Configuration
public class CrosConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1
        corsConfiguration.addAllowedHeader("*"); // 2
        corsConfiguration.addAllowedMethod("*"); // 3
        return corsConfiguration;
    }

    /*@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }*/

    private List<String> allowedOriginList = new ArrayList<>();

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
        //TODO:
        //config.addAllowedOrigin("*");

        //https://www.dbg-test.club:9700
        config.addAllowedOrigin("*");
        // 限制 HEADER 或 METHOD
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        /*
        CorsConfiguration allowAllConfig = new CorsConfiguration();
        allowAllConfig.addAllowedOrigin("*");
        allowAllConfig.addAllowedHeader("*");
        allowAllConfig.addAllowedMethod("*");

        source.registerCorsConfiguration("/api/**", allowAllConfig);
        */

        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));

        //设置在最前
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
