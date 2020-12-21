package com.fantechs.security.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

;

/**
 * @Auther: leifengzhi
 * @Date: 2020/9/16 15:03
 * @Description:
 * @Version: 1.0
 */
@RestController
@Api(tags = "登录控制器")
@Slf4j
public class LoginController {

    @Resource
    private SecurityFeignApi securityFeignApi;

    @PostMapping("/meslogin")
    @ApiOperation(value = "登陆接口")
    public ResponseEntity meslogin(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        ResponseEntity responseEntity = securityFeignApi.login(username, password);
        return  responseEntity;
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request){
        ResponseEntity responseEntity = securityFeignApi.logout();
        return responseEntity;
    }

    @ApiIgnore
    @PostMapping("/tologin")
    public ResponseEntity toLogin(){
        return ControllerUtil.returnFail(ErrorCodeEnum.GL99990401);
    }

    @ApiOperation(value = "刷新token")
    @PostMapping("refreshtoken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token=request.getHeader("token");
        String refreshToken=request.getHeader("refreshToken");
        if(StringUtils.isEmpty(token,refreshToken)){
            return ControllerUtil.returnFail(ErrorCodeEnum.UAC10011039);
        }
        try {
             token= TokenUtil.replaceToken(request.getHeader("user-agent"),token,refreshToken);
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(ErrorCodeEnum.UAC10011039);
        }
        response.setHeader("token",token);
        response.setHeader("Access-Control-Expose-Headers","token");
        return ControllerUtil.returnDataSuccess(token, StringUtils.isEmpty(token)?0:1);
    }


    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/userinfo")
    @ResponseBody
    public ResponseEntity<SysUser> getUserinfo() throws TokenValidationFailedException {
        SysUser loginUser = CurrentUserInfoUtils.getCurrentUserInfo();
        return ControllerUtil.returnDataSuccess(loginUser, StringUtils.isEmpty(loginUser)?0:1);
    }
}
