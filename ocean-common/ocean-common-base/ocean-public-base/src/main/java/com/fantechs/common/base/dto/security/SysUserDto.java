package com.fantechs.common.base.dto.security;

import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lfz on 2020/8/15.
 */
@Data
@Table(name = "sys_user")
public class SysUserDto extends SysUser implements UserDetails,Serializable {

    private static final long serialVersionUID = -1710596362169456736L;
    @Transient
    @ApiModelProperty(name="smtRole" ,value="角色对象")
    @JSONField(serialize = false)
    private List<SysRole> roles;

    @Transient
    @ApiModelProperty(name ="menuList",value = "角色权限对应的菜单树")
    private List<SysMenuInListDTO> menuList;

    /**
     * token
     */
    @Transient
    @ApiModelProperty(name="token" ,value="token")
    private String token;

    /**
     * refreshToken
     */
    @Transient
    @ApiModelProperty(name="refreshToken" ,value="refreshToken")
    private String refreshToken;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleId().toString()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
