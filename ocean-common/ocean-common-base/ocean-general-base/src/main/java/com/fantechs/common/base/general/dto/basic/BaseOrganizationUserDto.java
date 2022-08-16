package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseOrganizationUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class BaseOrganizationUserDto extends BaseOrganizationUser implements Serializable {

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode",value = "组织编码")
    @Column(name = "organization_code")
    @NotBlank(message = "组织编码不能为空")
    private String organizationCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @NotBlank(message = "组织名称不能为空")
    private String organizationName;

    /**
     * 组织描述
     */
    @ApiModelProperty(name="organizationDesc",value = "组织描述")
    @Column(name = "organization_desc")
    private String organizationDesc;
}
