package com.fantechs.security.config;


import com.fantechs.security.filter.CustomAuthenticationDetailsSource;
import com.fantechs.security.securityIntercepter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;


@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private  CustomAuthenticationProvider customAuthenticationProvider;
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

    @Autowired
    private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

    private String[] white_list={
//            "/*/**"
            "/tologin","/meslogin","/loginByOrgCode","/pda/login","/eamlogin",
            "/index.html", "/static/**", "/favicon.ico",
            "/swagger-ui.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/security"
            ,"null/swagger-resources/**","/sysSpecItem/findList","/sysRole/findList","/sysUser/saveByApi","/sysApiLog/add","/sysSpecItem/detail","/clientGetToken"
    };


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring()
                .antMatchers(white_list);
    }

    //????????????????????????????????????
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //????????????????????????service
        //??????userService?????????????????????UserDetail?????????security??????????????????
        //userService????????????UserDetailService?????????????????????
        auth.authenticationProvider(customAuthenticationProvider);
    }
    //????????????????????????????????????
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
                .authenticationDetailsSource(customAuthenticationDetailsSource)
                .successHandler(myAuthenticationSuccessHandler)//?????????????????????????????????
                .failureHandler(myAuthenticationFailHandler)//?????????????????????
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(myLogoutSuccessHandler)//?????????????????????
                .permitAll().
                and().csrf().disable()
//                .addFilterAt(new MyUsernamePasswordAuthenticationFilter(authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
    }

    //????????????
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://10.182.163.82:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
