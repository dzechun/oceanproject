package com.fantechs.common.base.general.entity.qms.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsPdaInspection extends BaseQuery implements Serializable {


    /**
     * PDA质检单号
     */
    @ApiModelProperty(name="pdaInspectionCode",value = "PDA质检单号")
    private String pdaInspectionCode;

    /**
     * 生产工单号
     */
    @ApiModelProperty(name = "workOrderCode",value = "生产工单号")
    private String workOrderCode;


    /**
     * 生产线
     */
    @ApiModelProperty(name="productionLine",value = "生产线")
    private String  productionLine;

    /**
     * 处理人
     */
    @ApiModelProperty(name="handler",value = "处理人")
    private String handler;

    /**
     * 栈板码
     */
    @ApiModelProperty(name="palletCode",value = "栈板码")
    private String palletCode;

    private static final long serialVersionUID = 1L;
}
