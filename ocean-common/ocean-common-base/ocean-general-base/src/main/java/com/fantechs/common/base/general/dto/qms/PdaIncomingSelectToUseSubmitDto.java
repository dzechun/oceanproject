package com.fantechs.common.base.general.dto.qms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * @date 2020-12-28 15:15:07
 */
@Data
public class PdaIncomingSelectToUseSubmitDto implements Serializable {

    /**
     * 来料检验单ID
     */
    @ApiModelProperty(name="incomingInspectionOrderId",value = "来料检验单ID")
    private Long incomingInspectionOrderId;

    /**
     * 总数量
     */
    @ApiModelProperty(name="OrderQty",value = "总数量")
    private BigDecimal OrderQty;

    /**
     * 挑选结果(0-退货 1-特采)
     */
    @ApiModelProperty(name="pickResult",value = "挑选结果(0-退货 1-特采)")
    private Byte pickResult;

    /**
     * 条码列表
     */
    @ApiModelProperty(name="barcodeDtoList",value = "条码列表")
    private List<PdaIncomingSelectToUseBarcodeDto> barcodeDtoList;

}
