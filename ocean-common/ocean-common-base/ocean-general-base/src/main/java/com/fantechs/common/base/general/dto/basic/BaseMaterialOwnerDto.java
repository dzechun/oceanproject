package com.fantechs.common.base.general.dto.basic;

import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author liangzhongwu
 * @create 2021-04-23 11:15
 */
@Data
public class BaseMaterialOwnerDto extends BaseMaterialOwner implements Serializable {

    private static final long serialVersionUID = -8778916568262883630L;

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
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
