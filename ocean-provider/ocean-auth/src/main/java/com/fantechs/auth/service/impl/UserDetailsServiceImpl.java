package com.fantechs.auth.service.impl;


import com.fantechs.common.base.dto.security.SysUserDto;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.auth.mapper.SysRoleMapper;
import com.fantechs.auth.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2020/6/5 18:05
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    protected static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",1).andEqualTo("userName"
                , s).orEqualTo("userCode",s);
        SysUser user = sysUserMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(user)){
            throw new BadCredentialsException("找不到用户");
        }
        SysUserDto userDto  = new SysUserDto();
        BeanUtils.copyProperties(user,userDto);
        List<SysRole> rolesByUserId= sysRoleMapper.findRolesByUserId(userDto.getUserId());
        if(StringUtils.isEmpty(rolesByUserId)){
            throw new BadCredentialsException("无访问权限");
        }
        userDto.setRoles(rolesByUserId);
        return userDto;
    }

    /**
     * 免密登录，用于内部
     * @param userNameOrCode
     */
    public void setUserDetail(String userNameOrCode, HttpServletRequest request) throws UsernameNotFoundException {

        // 根据用户名username加载userDetails
        UserDetails userDetails = this.loadUserByUsername(userNameOrCode);

        // 根据userDetails构建新的Authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());

        // 设置authentication中details
        //authentication.setDetails(new WebAuthenticationDetails(request));
        // 存放authentication到SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        // 在session中存放security context,方便同一个session中控制用户的其他操作
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }
}
