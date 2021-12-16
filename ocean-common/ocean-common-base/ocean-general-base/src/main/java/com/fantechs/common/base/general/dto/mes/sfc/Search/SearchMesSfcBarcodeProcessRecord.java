package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesSfcBarcodeProcessRecord extends BaseQuery implements Serializable {

    @ApiModelProperty(name="workOrderBarcodeId",value = "工单条码表ID")
    private Long workOrderBarcodeId;

    @ApiModelProperty(name="barcode",value = "产品条码")
    private String barcode;

    @ApiModelProperty(name="customerBarcode",value = "客户条码")
    private String customerBarcode;

    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    private String workOrderCode;

    @ApiModelProperty(name="barcodeType",value = "条码类别（1.工序流转卡、2.工单条码、3.客户条码）")
    private Byte barcodeType;

    @ApiModelProperty(name="stationId",value = "工位ID")
    private Long stationId;

    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    /**
     * 查询标识
     */
    @ApiModelProperty(name = "codeQueryMark",value = "查询标识 1 为等值查询 不<>1为模糊查询")
    private Integer codeQueryMark;
}
