package com.fantechs.auth.authIntercepter;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.TokenUtil;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: bingo.ren
 * @Date: 2020/6/3 15:44
 * @Description:
 * @Version: 1.0
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 如何带有token将token删除
        TokenUtil.clearTokenByRequest(httpServletRequest);
        StpUtil.logout();
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        ResponseEntity<Object> responseEntity = ControllerUtil.returnSuccess("退出成功");
        writer.write(JSONObject.toJSONString(responseEntity));
        writer.flush();
        writer.close();
    }
}
