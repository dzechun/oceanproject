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
    private final String organizationid;
    /**设备登录状态，1为密码登录 ，2为刷卡免登陆，系统正常登录可为空**/
    private final String type;


    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        organizationid = request.getParameter("orgId");;
        type = request.getParameter("type");
    }
    public String getOrganizationid() {
        return organizationid;
    }
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CustomWebAuthenticationDetails{" +
                "organizationid='" + organizationid + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
