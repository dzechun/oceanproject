package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerTransferSlipDto extends WmsInnerTransferSlip implements Serializable {

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="6")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 处理人
     */
    @Transient
    @ApiModelProperty(name = "processorUserName",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="11")
    private String processorUserName;

    /**
     * 组织编码
     */
    @Transient
    @ApiModelProperty(name = "organizationCode",value = "组织编码")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="12")
    private String organizationCode;
}
