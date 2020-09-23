package com.fantechs.common.base.utils;

import com.fantechs.common.base.entity.security.SysUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CurrentUserInfoUtils {

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static SysUser getCurrentUserInfo(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        SysUser user = null;
        if(StringUtils.isNotEmpty(token)){
            user= TokenUtil.load(token);
        }
        return user;
    }
}
