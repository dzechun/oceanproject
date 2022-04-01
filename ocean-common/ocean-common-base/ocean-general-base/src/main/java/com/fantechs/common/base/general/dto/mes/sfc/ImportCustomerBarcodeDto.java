package com.fantechs.common.base.general.dto.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImportCustomerBarcodeDto implements Serializable {

    @ApiModelProperty(name="salesCode",value = "销售编码(必填)")
    @Excel(name = "销售编码(必填)", height = 20, width = 30,orderNum="1")
    private String salesCode;

    @ApiModelProperty(name="startCode",value = "起(必填)")
    @Excel(name = "起(必填)", height = 20, width = 30,orderNum="2")
    private String startCode;

    @ApiModelProperty(name="endCode",value = "止(必填)")
    @Excel(name = "止(必填)", height = 20, width = 30,orderNum="3")
    private String endCode;
}
