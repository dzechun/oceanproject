package com.fantechs.security.config;



import com.fantechs.security.securityIntercepter.*;
import com.fantechs.security.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;


@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private MyAccessDecisionManager myAccessDecisionManager;
    @Resource
    private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;
    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Resource
    private MyAuthenticationFailHandler myAuthenticationFailHandler;
    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    private String[] white_list={
//            "/*/**"
            "/tologin","/meslogin","/pda/login",
            "/index.html", "/static/**", "/favicon.ico",
            "/swagger-ui.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/security"
            ,"null/swagger-resources/**","/sysSpecItem/findList","/sysSpecItem/detail"
    };


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring()
                .antMatchers(white_list);
    }

    //第一步配置用户详情数据源
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //需要传入用户详情service
        //配置userService需要实体类实现UserDetail接口供security使用实体信息
        //userService接口实现UserDetailService接口，重写方法
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    //第二步配置动态权限过滤器
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setAccessDecisionManager(myAccessDecisionManager);
                o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                return o;
            }
        }).and().cors().and()
                .formLogin()
                .loginPage("/tologin")
                .loginProcessingUrl("/login")
                .successHandler(myAuthenticationSuccessHandler)//可以配置登录成功的提示
                .failureHandler(myAuthenticationFailHandler)//登录失败的提示
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(myLogoutSuccessHandler)//退出登录的提示
                .permitAll().
                and().csrf().disable()
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
    }

    //配置跨域
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://10.182.163.82:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
