package com.fantechs.common.base.utils;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CurrentUserInfoUtils {
    private static RedisUtil redisUtil=(RedisUtil) SpringUtil.getBean(RedisUtil.class);

    public static String getToken() throws TokenValidationFailedException, BizErrorException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(StringUtils.isEmpty(requestAttributes)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404,"未找到请求");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
        }
        return token;
    }
    /**
     * 获取当前登录用户信息
     * @return
     */
    public static SysUser getCurrentUserInfo() throws TokenValidationFailedException, BizErrorException {
        String token = getToken();
        Object o= redisUtil.get(token);
        if(StringUtils.isEmpty(o)){
            throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
        }
        SysUser user = JSONObject.parseObject(JSONObject.toJSONString(o),SysUser.class);
        return user;
    }
}
