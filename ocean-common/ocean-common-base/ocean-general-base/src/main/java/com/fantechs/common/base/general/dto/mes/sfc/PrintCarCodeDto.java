package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrintCarCodeDto implements Serializable {

    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(name="labelCategoryId",value = "标签类别信息ID")
    private Long labelCategoryId;

    /**
     * 01 产品条码
     * 02 销售订单条码
     * 03 客户条码
     * 04 部件条码
     * 05 工单流转单
     * 06 工单流转卡
     * 07 工单流转卡条码
     * 08 彩盒条码
     * 09 包箱条码
     * 10 栈板条码
     */
    @ApiModelProperty(name="labelTypeCode",value = "标签类别编码(基础数据维护，目前已有以上10条)")
    private String labelTypeCode;

    @ApiModelProperty(name="barcode",value = "需打印的条码")
    private String barcode;

    @ApiModelProperty(name = "printName", value = "打印机名称")
    private String printName;

    @ApiModelProperty(name = "packingQty",value = "装箱数量")
    private String packingQty;
}
