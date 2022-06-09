package com.fantechs.auth.satokenconfigure;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.codingapi.txlcn.tracing.http.spring.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.Arrays;
import java.util.Collection;

/**
 * Create on 2022/5/31
 *
 * @author keguang_huang
 */
@Component
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer, AccessDecisionManager {

    private static Logger logger = LoggerFactory.getLogger(SaTokenConfigure.class);

    // 白名单
    private String[] white_list = {
            "/tologin","/logout", "/meslogin","/refreshtoken", "/loginByOrgCode", "/pda/login", "/eamlogin",
            "/index.html", "/static/**", "/favicon.ico", "/doLogin",
            "/swagger-ui.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/auth"
            , "null/swagger-resources/**", "/sysSpecItem/findList", "/sysRole/findList", "/sysUser/saveByApi", "/sysApiLog/add", "/sysSpecItem/detail", "/clientGetToken"
    };

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
    }


    /**
     * Sa-Token全局过滤器
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()

                // 指定 拦截路由
                .addInclude("/**")
                // 放行路由
                .addExclude(white_list)
                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    logger.info("进入Sa-Token全局认证");
                    // 登录认证 -- 拦截所有路由，并排除/login 用于开放登录
                    logger.info("登录状态：{}",StpUtil.isLogin());
                    SaRouter.match("/**", "/login", () -> StpUtil.checkLogin());
                })

                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    logger.info("进入Sa-Token异常处理");
                    return SaResult.error(e.getMessage());
                })

                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                            .back();
                });
    }

    // 配置跨域
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://10.182.163.82:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
