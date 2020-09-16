package com.fantechs.security.securityIntercepter;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.dto.security.SysUserDto;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.*;
import com.fantechs.security.utils.MySecurityTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static Logger log= LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);

    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        SysUserDto loginUser = MySecurityTool.getCurrentLoginUserDTO();
        if (StringUtils.isNotEmpty(loginUser)) {

            //---加入角色权限所对应的菜单名称
            List<SysRole> roles = loginUser.getRoles();
            List<String> roleIds = new  LinkedList<>();
            //menuType 1-web 2-pc 3-PDA
            int menuType=3;  //PDA
            log.info("===================agent:"+httpServletRequest.getHeader("user-agent"));
            if(httpServletRequest.getHeader("user-agent").contains("Apache-HttpClient")){
                menuType =1;
            }else if(!UserAgentUtil.CheckAgent(httpServletRequest.getHeader("user-agent"))){
                menuType=2;
            }
        }

        //如何带有token将token删除
        TokenUtil.clearTokenByRequest(httpServletRequest);
        //---生成token
        SysUser sysUser = MySecurityTool.getCurrentLoginUser();
//        sysUser.setAuthority(permsSet);

        String token = TokenUtil.generateToken(httpServletRequest.getHeader("user-agent"), sysUser,null);
        String refreshToken = TokenUtil.generateToken(httpServletRequest.getHeader("user-agent"), sysUser,getIpAddress(httpServletRequest));
        TokenUtil.save(token,sysUser);
        TokenUtil.save(refreshToken,sysUser);
        httpServletResponse.setHeader("token", token);
        httpServletResponse.setHeader("refreshToken",refreshToken);
        loginUser.setToken(token);
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "token,refreshToken");

        ResponseEntity<Object> responseEntity = ControllerUtil.returnDataSuccess(loginUser, 1);
        writer.write(JSONObject.toJSONString(responseEntity));
        writer.flush();
        writer.close();

    }

    /**
     * 获取用户真实ip地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

//    public  void  getPermsSet(List<SysMenuinListDTO> menuList){
//        for(SysMenuinListDTO SysMenuinListDTO:menuList){
//            if(StringUtils.isNotEmpty(SysMenuinListDTO.getSysMenuInfoDto().getUrl())){
//                permsSet.add(SysMenuinListDTO.getSysMenuInfoDto().getUrl());
//            }
//            if(StringUtils.isNotEmpty(SysMenuinListDTO.getSysMenuinList())){
//                getPermsSet(SysMenuinListDTO.getSysMenuinList());
//            }
//        }
//    }
}
