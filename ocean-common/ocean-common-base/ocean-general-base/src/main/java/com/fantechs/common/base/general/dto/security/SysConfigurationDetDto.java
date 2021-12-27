package com.fantechs.common.base.general.dto.security;

import com.fantechs.common.base.general.entity.security.SysConfigurationDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SysConfigurationDetDto extends SysConfigurationDet implements Serializable {
    private static final long serialVersionUID = -8402718231417737012L;
    /**
     * 配置名称
     */
    @Transient
    @ApiModelProperty(name = "configurationName",value = "配置名称")
    private String configurationName;
}
