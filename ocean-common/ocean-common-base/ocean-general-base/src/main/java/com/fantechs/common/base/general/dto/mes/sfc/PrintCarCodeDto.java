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

    @ApiModelProperty(name="barcodeType",value = "条码类别（1.工序流转卡、2.工单条码、3.客户条码、4-销售订单条码）")
    private Byte barcodeType;

    @ApiModelProperty(name="barcode",value = "需打印的条码")
    private String barcode;
}
