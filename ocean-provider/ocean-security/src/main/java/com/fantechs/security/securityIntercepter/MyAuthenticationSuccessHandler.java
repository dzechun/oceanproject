package com.fantechs.security.securityIntercepter;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.dto.security.SysUserDto;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.*;
import com.fantechs.security.service.SysMenuInfoService;
import com.fantechs.security.utils.MySecurityTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static Logger log= LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);

    // 菜单缓存redis的key
    private static String MENU_REDIS_KEY = "MENU_REDIS_KEY";

    @Autowired
    private SysMenuInfoService sysMenuInfoService;

    @Resource
    private RedisUtil redisUtil;

    private Set<String> permsSet = new HashSet<>();

    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        SysUserDto loginUser = MySecurityTool.getCurrentLoginUserDTO();
        List<SysMenuInListDTO>  roleMenuList = new LinkedList<>();
        if (StringUtils.isNotEmpty(loginUser)) {

            //---加入角色权限所对应的菜单名称
            List<SysRole> roles = loginUser.getRoles();
            List<String> roleIds = new  LinkedList<>();
            String browserKernel = httpServletRequest.getParameter("browserKernel");
            //menuType 1-windows(平板C端)访问  2-web(客户端) 3-PDA(移动设备) 4-安卓app访问系统
            int menuType = 3;  //PDA
            if(StringUtils.isEmpty(browserKernel)) {
                log.info("==========================" + httpServletRequest.getHeader("user-agent"));
                if (httpServletRequest.getHeader("user-agent").contains("Apache-HttpClient")) {
                    menuType = 1;
                } else if (!UserAgentUtil.CheckAgent(httpServletRequest.getHeader("user-agent"))) {
                    menuType = 2;
                }

            }else{
                menuType = Integer.valueOf(browserKernel);
            }

            if (StringUtils.isNotEmpty(roles)) {
                for (SysRole role : roles) {
                    roleIds.add(role.getRoleId().toString());
                }
                //构建角色菜单树
                roleMenuList = sysMenuInfoService.findMenuList(ControllerUtil.dynamicCondition(
                            "parentId", "0",
                            "menuType", menuType + ""
                ), roleIds);

                if (StringUtils.isNotEmpty(roleMenuList)) {
                    loginUser.setMenuList(roleMenuList);
                    //获取所有菜单地址
                    getPermsSet(roleMenuList);
                }
            }

        }

        //如何带有token将token删除
        TokenUtil.clearTokenByRequest(httpServletRequest);
        //---生成token
        SysUser sysUser = MySecurityTool.getCurrentLoginUser();
        sysUser.setAuthority(permsSet);
        String token = TokenUtil.generateToken(httpServletRequest.getHeader("user-agent"), sysUser,null);
        String refreshToken = TokenUtil.generateToken(httpServletRequest.getHeader("user-agent"), sysUser,getIpAddress(httpServletRequest));
        TokenUtil.save(token,sysUser);
        TokenUtil.save(refreshToken,sysUser);
        httpServletResponse.setHeader("token", token);
        httpServletResponse.setHeader("refreshToken",refreshToken);
        loginUser.setToken(token);
        loginUser.setRefreshToken(refreshToken);
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

    public  void  getPermsSet(List<SysMenuInListDTO> menuList){
        for(SysMenuInListDTO SysMenuinListDTO:menuList){
            if(StringUtils.isNotEmpty(SysMenuinListDTO.getSysMenuInfoDto().getUrl())){
                permsSet.add(SysMenuinListDTO.getSysMenuInfoDto().getUrl());
            }
            if(StringUtils.isNotEmpty(SysMenuinListDTO.getSysMenuinList())){
                getPermsSet(SysMenuinListDTO.getSysMenuinList());
            }
        }
    }
}
