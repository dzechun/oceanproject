package com.fantechs.auth.authIntercepter;

import com.fantechs.common.base.dto.security.SysUserDto;
import com.fantechs.common.base.entity.security.SysOrganizationUser;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.auth.filter.CustomWebAuthenticationDetails;
import com.fantechs.auth.mapper.SysOrganizationUserMapper;
import com.fantechs.auth.mapper.SysSpecItemMapper;
import com.fantechs.auth.mapper.SysUserMapper;
import com.fantechs.auth.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
/**
 * 自定义登录验证
 */
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private SysOrganizationUserMapper sysOrganizationUserMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SysSpecItemMapper sysSpecItemMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
     public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //用户输入的用户名
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        String organizationid = details.getOrganizationid();
        String type=details.getType();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        SysUserDto userDto = (SysUserDto) userDetails;
        if(StringUtils.isEmpty(userDto)){
            log.warn("找不到用户");
            throw new BadCredentialsException("找不到用户");
        }
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean f = bcryptPasswordEncoder.matches(password,userDetails.getPassword());

        //新宝刷卡登录（免密）
        if(StringUtils.isNotEmpty(details.getType()) && "2".equals(type)){
            logger.info("--------刷卡登录----------");
            userDto.setPassword(new BCryptPasswordEncoder().encode("123456"));
        }else {
            //校验用户密码
            if(!f){
                log.warn("密码错误");
                throw new BadCredentialsException("密码错误");
            }
         }
        Example specExample = new Example(SysSpecItem.class);
        specExample.createCriteria().andEqualTo("specCode","isOrg");
        SysSpecItem sysSpecItem = sysSpecItemMapper.selectOneByExample(specExample);
        String paraValue = sysSpecItem.getParaValue();
        if(Integer.valueOf(paraValue) == 1 && StringUtils.isNotEmpty(organizationid)){
            SysOrganizationUser searchOrganizationUser =  new SysOrganizationUser();
            searchOrganizationUser.setUserId(userDto.getUserId());
            searchOrganizationUser.setOrganizationId(new Long(organizationid));
            SysOrganizationUser sysOrganizationUsers = sysOrganizationUserMapper.selectOne(searchOrganizationUser);
            SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
            searchBaseOrganization.setOrgId(new Long(organizationid));
            List<BaseOrganizationDto> orgList = baseFeignApi.findOrganizationList(searchBaseOrganization).getData();
            if(StringUtils.isNotEmpty(sysOrganizationUsers,orgList) && orgList.get(0).getStatus() != 0){
                userDto.setOrganizationId(new Long(organizationid));
            }else{
                throw new BadCredentialsException("组织错误");
            }
        }else{
            List<Long> organizationList = sysUserMapper.findOrganizationList(userDto.getUserId());
            if (StringUtils.isNotEmpty(organizationList)){
                userDto.setOrganizationId(organizationList.get(0));
            }
        }
        Object principalToReturn = userDto;
        return this.createSuccessAuthentication(principalToReturn, authentication, userDto);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
             return authentication.equals(UsernamePasswordAuthenticationToken.class);
     }
}
