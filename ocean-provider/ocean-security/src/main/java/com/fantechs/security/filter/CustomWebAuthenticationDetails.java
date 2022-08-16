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
    /**设备登录类型，不传则按照浏览器内核区分设备（以前的方式），生成token
     * 1-windows(平板C端)访问  2-web(客户端) 3-PDA(移动设备) 4-安卓app访问系统
     * **/
    private final String browserKernel;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        organizationid = request.getParameter("orgId");;
        type = request.getParameter("type");
        browserKernel = request.getParameter("browserKernel");
    }
    public String getOrganizationid() {
        return organizationid;
    }
    public String getType() {
        return type;
    }
    public String getBrowserKernel() {
        return browserKernel;
    }


    @Override
    public String toString() {
        return "CustomWebAuthenticationDetails{" +
                "organizationid='" + organizationid + '\'' +
                ", type='" + type + '\'' +
                ", browserKernel='" + browserKernel + '\'' +
                '}';
    }
}
