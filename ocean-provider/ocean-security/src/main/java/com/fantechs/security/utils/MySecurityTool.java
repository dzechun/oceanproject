package com.fantechs.security.utils;


import com.fantechs.common.base.dto.security.SysUserDto;
import com.fantechs.common.base.entity.security.SysUser;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Auther: bingo.ren
 * @Date: 2020/6/8 11:16
 * @Description:
 * @Version: 1.0
 */
public class MySecurityTool {

    public static SysUserDto getCurrentLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            SysUserDto tSysUserDTO = (SysUserDto) authentication.getPrincipal();
            SysUserDto mbUser = new SysUserDto();
            BeanUtils.copyProperties(tSysUserDTO, mbUser);
            return mbUser;
        }
        return null;
    }

    public static SysUserDto getCurrentLoginUserDTO(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            SysUserDto tSysUserDTO = (SysUserDto) authentication.getPrincipal();
            return tSysUserDTO;
        }
        return null;
    }
}
