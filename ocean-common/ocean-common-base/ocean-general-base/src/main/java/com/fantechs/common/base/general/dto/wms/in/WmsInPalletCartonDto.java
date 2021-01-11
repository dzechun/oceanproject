package com.fantechs.common.base.general.dto.wms.in;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.in.WmsInPalletCarton;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;
import java.io.Serializable;

public class WmsInPalletCartonDto extends WmsInPalletCarton implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode" ,value="组织编码")
    private String organizationCode;

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
}
