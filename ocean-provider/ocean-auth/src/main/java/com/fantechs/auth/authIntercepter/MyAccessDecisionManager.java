package com.fantechs.auth.authIntercepter;

import cn.dev33.satoken.stp.StpUtil;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.auth.service.impl.UserDetailsServiceImpl;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;


/**
 * 权限决策器，判断当前用户是否具有当前访问URL相关的权限
 */
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {
    @Resource
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     *
     * @param authentication 存放了当前登录用户的所有信息
     * @param o 获取当前请求对象等
     * @param collection filterInvocationSecurityMetadataSource存入的当前需要的权限
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        if(!(authentication instanceof UsernamePasswordAuthenticationToken)){
            //---有可能是token访问
//            FilterInvocation filterInvocation = (FilterInvocation) o;
//            HttpServletRequest request = filterInvocation.getRequest();
//            String token = request.getHeader("token");
            String token = StpUtil.getTokenValue();
            if(StringUtils.isNotEmpty(token) && StpUtil.isLogin()){
                SysUser mbUser = TokenUtil.load(token);
                try {
                        userDetailsServiceImpl.setUserDetail(mbUser.getUserCode(),null);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AccessDeniedException(e.getMessage());
                }
                authentication= SecurityContextHolder.getContext().getAuthentication();
            }
        }

        //调用实体类的getAuthorities获取当前用户的权限信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (ConfigAttribute configAttribute : collection) {
            //如果需要的游客，则直接放行
            switch (configAttribute.getAttribute()){
                case "ROLE_guest":return;
                case "ROLE_login":if(authentication instanceof UsernamePasswordAuthenticationToken)return;
            }
            for (GrantedAuthority authority : authorities) {
                if(configAttribute.getAttribute().equals(authority.getAuthority())){
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足");
    }

    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    public boolean supports(Class<?> aClass) {
        return true;
    }
}
