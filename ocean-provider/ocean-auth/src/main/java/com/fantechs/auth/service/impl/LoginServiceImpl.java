package com.fantechs.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fantechs.auth.mapper.SysOrganizationUserMapper;
import com.fantechs.auth.mapper.SysSpecItemMapper;
import com.fantechs.auth.mapper.SysUserMapper;
import com.fantechs.auth.service.LoginService;
import com.fantechs.auth.service.SysMenuInfoService;
import com.fantechs.auth.service.SysRoleService;
import com.fantechs.auth.service.SysUserService;
import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.dto.security.SysUserDto;
import com.fantechs.common.base.entity.security.SysOrganizationUser;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.common.base.utils.UserAgentUtil;
import com.fantechs.provider.api.base.BaseFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Create on 2022/6/1
 *
 * @author keguang_huang
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private SysMenuInfoService sysMenuInfoService;
    @Autowired
    private BaseFeignApi baseFeignApi;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysSpecItemMapper sysSpecItemMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private SysOrganizationUserMapper sysOrganizationUserMapper;
    @Autowired
    private SysUserService sysUserService;

    private Set<String> permsSet = new HashSet<>();

    @Override
    public ResponseEntity mesLogin(String userName, String password, Long orgId, String type,String browserKernel) {
        //验证账号密码
        SysUserDto loginUser = verifyUserInfo(userName, password, orgId.toString(), type);
        if (ObjectUtil.isNull(loginUser) || StringUtils.isEmpty(loginUser.getUserId())) {
            log.warn("用户不存在");
            throw new BadCredentialsException("用户不存在");
        }

        // 组装用户信息
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userAgent = httpServletRequest.getHeader("user-agent");
        List<SysMenuInListDTO> roleMenuList = new LinkedList<>();
        List<SysRole> roles = sysRoleService.findSysRoleList(loginUser.getUserId());
        if (CollectionUtils.isEmpty(roles)) {
            log.warn("用户不无分配角色");
            throw new BadCredentialsException("用户不无分配角色");
        }

        // 加入角色权限所对应的菜单名称
        if (StringUtils.isNotEmpty(loginUser)) {
            List<String> roleIds = new LinkedList<>();

            // menuType 1-windows(平板C端)访问  2-web(客户端) 3-PDA(移动设备) 4-安卓app访问系统
            int menuType = 3;  //PDA
            if (StringUtils.isEmpty(browserKernel)) {
                logger.info("user-agent：" + httpServletRequest.getHeader("user-agent"));
                if (userAgent.contains("Apache-HttpClient")) {
                    menuType = 1;
                } else if (!UserAgentUtil.CheckAgent(httpServletRequest.getHeader("user-agent"))) {
                    menuType = 2;
                }
            } else {
                menuType = Integer.valueOf(browserKernel);
            }

            // 获取菜单权限列表
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

        // 如果登录请求头带有token，会删除旧的token
        TokenUtil.clearTokenByRequest(httpServletRequest);
        SysUser sysUser = sysUserService.selectByKey(loginUser.getUserId());
        sysUser.setAuthority(permsSet);
        sysUser.setOrganizationId(orgId);
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(sysUser.getUserId());
        if (StringUtils.isNotEmpty(orgId)) {
            searchBaseSupplierReUser.setOrgId(orgId);
        }

        // 获取用户绑定供应商列表
        List<BaseSupplierReUser> baseSupplierReUserList = baseFeignApi.findList(searchBaseSupplierReUser).getData();
        if (StringUtils.isNotEmpty(baseSupplierReUserList)) {
            sysUser.setSupplierId(baseSupplierReUserList.get(0).getSupplierId());
            sysUser.setSupplierCode(baseSupplierReUserList.get(0).getSupplierCode());
            sysUser.setSupplierName(baseSupplierReUserList.get(0).getSupplierName());
        }

        //sa-token登录
        String token = TokenUtil.generateToken(userAgent, sysUser);
        String refreshToken = TokenUtil.generateRefreshToken(userAgent, sysUser,getIpAddress(httpServletRequest));
        TokenUtil.saveToken(token, sysUser);
        TokenUtil.saveRefreshToken(refreshToken, sysUser);
        loginUser.setToken(token);
        loginUser.setRefreshToken(refreshToken);
        return ControllerUtil.returnDataSuccess(loginUser, 1);
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        try{
            // 将redis的token删除
            TokenUtil.clearTokenByRequest(request);
            StpUtil.logout();
            return true;
        }catch (Exception e){
            logger.error("退出登录异常：",e);
            return false;
        }
    }

    /**
     * 验证用户信息（改造原有登录方法）
     *
     * @param userName       密码
     * @param password       账号
     * @param organizationId 1-windows(平板C端)访问  2-web(客户端) 3-PDA(移动设备) 4-安卓app访问系统
     * @param type           设备登录状态，1为密码登录 ，2为刷卡免登陆，系统正常登录可为空
     * @return 用户id
     */
    private SysUserDto verifyUserInfo(String userName, String password, String organizationId, String type) {
        // 用户输入的用户名
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        SysUserDto userDto = (SysUserDto) userDetails;
        if (StringUtils.isEmpty(userDto)) {
            log.warn("不存在该用户");
            throw new BadCredentialsException("找不到用户");
        }
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean f = bcryptPasswordEncoder.matches(password, userDetails.getPassword());

        // 新宝刷卡登录（免密）
        if (StringUtils.isNotEmpty(type) && "2".equals(type)) {
            logger.info("--------刷卡登录----------");
            userDto.setPassword(new BCryptPasswordEncoder().encode("123456"));
        } else {
            //校验用户密码
            if (!f) {
                log.warn("密码错误");
                throw new BadCredentialsException("密码错误");
            }
        }
        Example specExample = new Example(SysSpecItem.class);
        specExample.createCriteria().andEqualTo("specCode", "isOrg");
        SysSpecItem sysSpecItem = sysSpecItemMapper.selectOneByExample(specExample);
        String paraValue = sysSpecItem.getParaValue();
        if (Integer.valueOf(paraValue) == 1 && StringUtils.isNotEmpty(organizationId)) {
            SysOrganizationUser searchOrganizationUser = new SysOrganizationUser();
            searchOrganizationUser.setUserId(userDto.getUserId());
            searchOrganizationUser.setOrganizationId(new Long(organizationId));
            SysOrganizationUser sysOrganizationUsers = sysOrganizationUserMapper.selectOne(searchOrganizationUser);
            SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
            searchBaseOrganization.setOrgId(new Long(organizationId));
            List<BaseOrganizationDto> orgList = baseFeignApi.findOrganizationList(searchBaseOrganization).getData();
            if (StringUtils.isNotEmpty(sysOrganizationUsers, orgList) && orgList.get(0).getStatus() != 0) {
                userDto.setOrganizationId(new Long(organizationId));
            } else {
                throw new BadCredentialsException("组织错误");
            }
        } else {
            List<Long> organizationList = sysUserMapper.findOrganizationList(userDto.getUserId());
            if (StringUtils.isNotEmpty(organizationList)) {
                userDto.setOrganizationId(organizationList.get(0));
            }
        }
        return userDto;
    }

    /**
     * 设置菜单权限
     *
     * @param menuList
     */
    public void getPermsSet(List<SysMenuInListDTO> menuList) {
        for (SysMenuInListDTO SysMenuinListDTO : menuList) {
            if (StringUtils.isNotEmpty(SysMenuinListDTO.getSysMenuInfoDto().getUrl())) {
                permsSet.add(SysMenuinListDTO.getSysMenuInfoDto().getUrl());
            }
            if (StringUtils.isNotEmpty(SysMenuinListDTO.getSysMenuinList())) {
                getPermsSet(SysMenuinListDTO.getSysMenuinList());
            }
        }
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
}
