package com.fantechs.auth.authIntercepter;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAuthenticationFailHandler implements AuthenticationFailureHandler {
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        String message = e.getMessage();
        ResponseEntity<Object> responseEntity = ControllerUtil.returnFail(
                message.contains("权限")? ErrorCodeEnum.GL99990401.getMsg(): (message.contains("组织")? message: ErrorCodeEnum.UAC10011016.getMsg()),
                message.contains("权限")? ErrorCodeEnum.GL99990401.getCode(): ErrorCodeEnum.UAC10011016.getCode());
        writer.write(JSONObject.toJSONString(responseEntity));
        writer.flush();
        writer.close();
    }
}
