package com.fantechs.auth.satokenconfigure;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.dev33.satoken.util.SaResult;
import com.codingapi.txlcn.tracing.http.spring.WebMvcConfigurer;
import com.fantechs.auth.authIntercepter.MyAuthenticationSuccessHandler;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.UserAgentUtil;
import cz.mallat.uasparser.UserAgentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * Create on 2022/5/31
 *
 * @author keguang_huang
 */
@Component
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer, AccessDecisionManager {

    private static Logger logger= LoggerFactory.getLogger(SaTokenConfigure.class);

    private String[] white_list={
            "/tologin","/meslogin","/loginByOrgCode","/pda/login","/eamlogin",
            "/index.html", "/static/**", "/favicon.ico","/doLogin",
            "/swagger-ui.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/auth"
            ,"null/swagger-resources/**","/sysSpecItem/findList","/sysRole/findList","/sysUser/saveByApi","/sysApiLog/add","/sysSpecItem/detail","/clientGetToken"
    };
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册Sa-Token的路由拦截器
//        registry.addInterceptor(new SaRouteInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(white_list);
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

                // 指定 拦截路由 与 放行路由
                .addInclude("/**").addExclude(white_list)

                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    System.out.println("---------- 进入Sa-Token全局认证 -----------");
                    // 登录认证 -- 拦截所有路由，并排除/doLogin 用于开放登录
                    SaRouter.match("/**", "/meslogin", () -> StpUtil.checkLogin());
                })

                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    logger.info("进入Sa-Token异常处理");
                    return SaResult.error(e.getMessage());
                })

                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(r -> {
                    // ---------- 设置一些安全响应头 ----------
                    SaHolder.getResponse()
                            // 服务器名称
                            .setServer("ocean-auth")
                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff")
                    ;
                })
                ;
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
