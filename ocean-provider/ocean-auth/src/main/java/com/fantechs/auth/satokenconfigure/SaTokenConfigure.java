package com.fantechs.auth.satokenconfigure;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import com.codingapi.txlcn.tracing.http.spring.WebMvcConfigurer;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.UserAgentUtil;
import cz.mallat.uasparser.UserAgentInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String[] white_list={
            "/tologin","/meslogin","/loginByOrgCode","/pda/login","/eamlogin",
            "/index.html", "/static/**", "/favicon.ico","/doLogin",
            "/swagger-ui.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/auth"
            ,"null/swagger-resources/**","/sysSpecItem/findList","/sysRole/findList","/sysUser/saveByApi","/sysApiLog/add","/sysSpecItem/detail","/clientGetToken"
    };
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的路由拦截器
        registry.addInterceptor(new SaRouteInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(white_list);
    }

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        boolean is = StpUtil.isLogin();
        System.out.println("");
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
