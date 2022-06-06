package com.fantechs.auth.authIntercepter;


import cn.dev33.satoken.stp.StpUtil;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.auth.service.SysMenuInfoService;
import com.fantechs.auth.service.SysUserService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 定义权限过滤器，查看当前访问链接需要哪些权限
 */
@Component
public class MyFilterInvocationSecurityMetadataSource  {
    @Resource
    private SysMenuInfoService SysMenuinfoService;
    private AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Resource
    private SysUserService sysUserService;

    /**
     * 获取访问某一个url所需的角色
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
//        FilterInvocation filterInvocation = (FilterInvocation) o;
//        HttpServletRequest httpRequest = filterInvocation.getHttpRequest();


//        String token = httpRequest.getHeader("token");
        String token = StpUtil.getTokenValue();
        if (StringUtils.isNotEmpty(token)){
            SysUser user = TokenUtil.load(token);
            if(StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(user.getAuthority())){
                StringBuffer sb = new StringBuffer();
                String path = null;//httpRequest.getServletPath();
                String[] pathArr =  path.split("/");
                if(pathArr.length>2){
                    Set<String> authority = user.getAuthority();
                    if(authority.contains(path)){
                        List<String> allRoleId = sysUserService.findAllRoleId(user.getUserId());
                        return SecurityConfig.createList(allRoleId.toArray(new String[allRoleId.size()]));
                    }
                }
            }
            return SecurityConfig.createList("ROLE_guest");
        }

        //如果当前链接并没有匹配到任何权限
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

//    public boolean supports(Class<?> aClass) {
//        return FilterInvocation.class.isAssignableFrom(aClass);
//    }
}
