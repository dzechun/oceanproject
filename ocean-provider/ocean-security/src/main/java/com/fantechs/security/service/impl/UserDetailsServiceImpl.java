package com.fantechs.security.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysUserDto;
import com.fantechs.common.base.entity.security.SysOrganizationUser;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.filter.CustomWebAuthenticationDetails;
import com.fantechs.security.mapper.SysOrganizationUserMapper;
import com.fantechs.security.mapper.SysRoleMapper;
import com.fantechs.security.mapper.SysSpecItemMapper;
import com.fantechs.security.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
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

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysOrganizationUserMapper sysOrganizationUserMapper;
    @Resource
    private SysSpecItemMapper sysSpecItemMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",1).andEqualTo("userName"
                , s).orEqualTo("userCode",s);
        SysUser user = sysUserMapper.selectOneByExample(example);

        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011011,s);
        }


        List<SysRole> rolesByUserId=new LinkedList<>();
        try{
            rolesByUserId = sysRoleMapper.findRolesByUserId(user.getUserId());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        if(StringUtils.isEmpty(rolesByUserId)){
            throw new BizErrorException(ErrorCodeEnum.GL99990401);
        }

        Example specExample1 = new Example(SysSpecItem.class);
        specExample1.createCriteria().andEqualTo("specCode","isOrg");
        SysSpecItem sysSpecItem = sysSpecItemMapper.selectOneByExample(specExample1);
        String paraValue = sysSpecItem.getParaValue();
        if(Integer.valueOf(paraValue) == 1 && StringUtils.isNotEmpty(CustomWebAuthenticationDetails.ORGANIZATIONID)){
            Example example1 = new Example(SysOrganizationUser.class);
            example1.createCriteria().andEqualTo("userId",user.getUserId());
            List<SysOrganizationUser> sysOrganizationUsers = sysOrganizationUserMapper.selectByExample(example1);
            boolean b = false;
            for (SysOrganizationUser sysOrganizationUser : sysOrganizationUsers) {
                if (sysOrganizationUser.getOrganizationId().equals(new Long(CustomWebAuthenticationDetails.ORGANIZATIONID))){
                    b = true;
                }
            }
            if (b){
                user.setOrganizationId(new Long(CustomWebAuthenticationDetails.ORGANIZATIONID));
            }else{
                CustomWebAuthenticationDetails.pass =false;
                throw new BizErrorException("组织错误");
            }
        }else{
            List<Long> organizationList = sysUserMapper.findOrganizationList(user.getUserId());
            if (StringUtils.isNotEmpty(organizationList)){
                user.setOrganizationId(organizationList.get(0));
            }
        }

        SysUserDto userDto  = new SysUserDto();
        BeanUtils.copyProperties(user,userDto);
        userDto.setRoles(rolesByUserId);
        //新宝刷卡登录（免密）
        if(StringUtils.isNotEmpty(CustomWebAuthenticationDetails.TYPE) && "2".equals(CustomWebAuthenticationDetails.TYPE)){
            logger.info("--------刷卡登录----------");
            userDto.setPassword(new BCryptPasswordEncoder().encode("123456"));
        }
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
        authentication.setDetails(new WebAuthenticationDetails(request));
        // 存放authentication到SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        // 在session中存放security context,方便同一个session中控制用户的其他操作
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }
}
