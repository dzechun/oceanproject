package com.fantechs.common.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Order(1)
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        //添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("*");
        // 是否发送Cookie信息
        config.setAllowCredentials(true);
        // 允许的请求方式
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        // 允许HTTP请求携带那些头部信息
        config.addAllowedHeader("*");

        //添加映射路径，/** 表示对所有路径实行全局跨域访问权限的限制
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        //返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}
