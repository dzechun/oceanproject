package com.fantechs.common.base.config;

import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by lfz on 2020/8/11.
 */
@Configuration
public class FeignConfig implements RequestInterceptor {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(StringUtils.isNotEmpty(attributes)){
            HttpServletRequest request = attributes.getRequest();
            //添加token
            requestTemplate.header("token", request.getHeader("token"));
            requestTemplate.header("user-agent", request.getHeader("user-agent"));
        }
        else {
            Set<String> set = redisUtil.keys("client_token*");
            if (set.size() > 0) {
                for (String str : set) {
                    requestTemplate.header("token", str);
                    break;
                }
            }
        }



    }
}
