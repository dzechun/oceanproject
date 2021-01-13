package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNote;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class WmsOutShippingNoteDto extends WmsOutShippingNote implements Serializable {

    /**
     * 操作人
     */
    @ApiModelProperty(name="operatorUserName",value = "操作人")
    @Excel(name = "操作人", height = 20, width = 30,orderNum="4")
    private String operatorUserName;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserName",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="5")
    private String processorUserName;

    /**
     * 组织代码
     */
    @ApiModelProperty(name="organizationCode",value = "组织代码")
    @Excel(name = "组织代码", height = 20, width = 30,orderNum="1")
    private String organizationCode;

}
