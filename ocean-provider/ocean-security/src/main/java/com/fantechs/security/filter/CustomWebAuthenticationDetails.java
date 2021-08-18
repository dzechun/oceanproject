package com.fantechs.security.filter;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lfz on 2021/4/9.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 6975601077710753878L;
    public static String ORGANIZATIONID;
    public static String TYPE;      //设备登录状态，1为密码登录 ，2为刷卡免登陆，系统正常登录可为空
    public static boolean pass = true;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        ORGANIZATIONID = request.getParameter("orgId");
        TYPE = request.getParameter("type");
    }
}
