package com.fantechs.common.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义表单工具类
 */
public class CustomFormUtils {

    private static Logger logger = LoggerFactory.getLogger(CustomFormUtils.class);

    public static String getFromRout() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String fromRout = httpServletRequest.getHeader("fromRout");
        logger.info("自定义导出接收header中fromRout参数：{}", fromRout);
        return fromRout;
    }
}
