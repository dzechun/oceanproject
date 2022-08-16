package com.fantechs.common.base.dto.security;

import com.fantechs.common.base.entity.security.SysMenuInfo;
import com.fantechs.common.base.entity.security.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lfz on 2020/9/14.
 */
@Data
public class SysMenuInfoDto extends SysMenuInfo implements Serializable {

    private static final long serialVersionUID = 5411033714021014085L;
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 对应的角色集合
     */
    @Transient
    @ApiModelProperty(name = "roles",value = "对应的角色集合")
    private List<SysRole> roles=new LinkedList<>();

}
