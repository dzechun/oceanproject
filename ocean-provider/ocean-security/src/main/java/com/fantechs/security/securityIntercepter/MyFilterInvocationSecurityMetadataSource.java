package com.fantechs.security.securityIntercepter;


import com.fantechs.common.base.dto.security.SysMenuInfoDto;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.security.service.SysMenuInfoService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 定义权限过滤器，查看当前访问链接需要哪些权限
 */
@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Resource
    private SysMenuInfoService SysMenuinfoService;
    private AntPathMatcher antPathMatcher=new AntPathMatcher();

    /**
     * 获取访问某一个url所需的角色
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) o;
        String requestUrl =filterInvocation.getRequestUrl();
        //获取所有菜单，遍历判断当前菜单需要的权限
        SysMenuInfoDto sysMenuinfo = SysMenuinfoService.findByMap(ControllerUtil.dynamicCondition(
                "url", requestUrl
        ));

        if(sysMenuinfo != null){
            if(antPathMatcher.match(sysMenuinfo.getUrl(), requestUrl) && sysMenuinfo.getRoles().size()>0 && sysMenuinfo.getIsMenu()==1){
                List<SysRole> tSysRoles = sysMenuinfo.getRoles();
                String[] roleArray = new String[tSysRoles.size()];
                for (int i = 0; i < roleArray.length; i++) {
                    roleArray[i]=tSysRoles.get(i).getRoleId().toString();
                }
                return SecurityConfig.createList(roleArray);
            }
        }
        //如果当前链接并没有匹配到任何权限，那就赋予一个最基本的权限，游客
        return SecurityConfig.createList("ROLE_guest");
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
