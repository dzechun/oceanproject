package com.fantechs.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lfz on 2021/4/9.
 */
@Slf4j
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 6975601077710753878L;
    public static String ORGANIZATIONID=null;
    public static String TYPE;      //设备登录状态，1为密码登录 ，2为刷卡免登陆，系统正常登录可为空

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        ORGANIZATIONID = request.getParameter("orgId");
        log.info("======================登录传入组织ID："+ORGANIZATIONID);
        TYPE = request.getParameter("type");
    }
}
