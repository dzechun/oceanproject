package com.fantechs.security.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.security.service.SysLoginByEquipmentService;
import com.fantechs.security.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysLoginByEquipmentService sysLoginByEquipmentService;

    @Resource
    private BaseFeignApi baseFeignApi;




    @PostMapping("/meslogin")
    @ApiOperation(value = "登陆接口")
    public ResponseEntity meslogin(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestParam(value = "organizationId") Long orgId){
        ResponseEntity responseEntity = securityFeignApi.login(username, password,orgId,null);
        return  responseEntity;
    }

    @PostMapping("/meslogin")
    @ApiOperation(value = "登陆接口")
    public ResponseEntity meslogin(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestParam(value = "orgCode") String orgCode){
        SearchBaseOrganization organization = new SearchBaseOrganization();
        organization.setOrganizationCode(orgCode);
        List<BaseOrganizationDto> organizationDtos = baseFeignApi.findOrganizationList(organization).getData();
        if (organizationDtos.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404, "此组织编码不存在或已被删除，不可登录");
        }
        ResponseEntity responseEntity = securityFeignApi.login(username, password, organizationDtos.get(0).getOrganizationId(),null);
        return responseEntity;
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

    @ApiOperation(value = "为客户端赋予一个可访问的token")
    @GetMapping("/clientGetToken")
    public ResponseEntity<String> clientGetToken(@RequestParam(value = "orgId") Long orgId) {

        log.info("--------------为客户端赋予一个可访问的token--------------");
        SysUser sysUser = sysUserService.selectByCode("admin");
        sysUser.setOrganizationId(orgId);
        String token = "client_" + TokenUtil.generateToken("", sysUser, null);
        TokenUtil.save(token, sysUser);
        log.info("--------------返回一个可访问的token : " + token + "--------------");

        return ControllerUtil.returnDataSuccess("生成client_token成功", token);
    }


    @PostMapping("/eamlogin")
    @ApiOperation(value = "设备登陆接口")
    public ResponseEntity mesloginByEam(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestParam(value = "organizationId") Long orgId
            , @RequestParam(value = "mac") String mac ,@RequestParam(value = "type") String type ){
        return  sysLoginByEquipmentService.eamLogin(username,password,orgId,mac,type);
    }
}
