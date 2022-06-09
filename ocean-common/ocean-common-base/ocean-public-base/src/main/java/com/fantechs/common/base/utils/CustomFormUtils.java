package com.fantechs.common.base.utils;

import com.fantechs.common.base.exception.BizErrorException;
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

    /**
     * 获取请求头中fromRout参数
     * @return
     */
    public static String getFromRout() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String fromRout = httpServletRequest.getHeader("fromRout");
        if (StringUtils.isEmpty(fromRout)) {
            throw new BizErrorException("错误：无法获取表单路由参数（fromRout）");
        }
        logger.info("自定义导出接收header中fromRout参数：{}", fromRout);
        return fromRout;
    }
}
