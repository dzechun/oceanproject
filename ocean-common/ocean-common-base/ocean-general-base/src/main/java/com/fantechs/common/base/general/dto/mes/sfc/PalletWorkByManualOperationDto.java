package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PalletWorkByManualOperationDto implements Serializable {

    @ApiModelProperty(name="proLineId",value = "产线ID")
    private Long proLineId;

    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    @ApiModelProperty(name="stationId",value = "工位ID")
    private Long stationId;

    @ApiModelProperty(name="wanbaoBarcodeDtos",value = "条码")
    private List<WanbaoBarcodeDto> wanbaoBarcodeDtos;

    @ApiModelProperty(name = "checkdaliyOrder", value = "检查排程单（0-否 1-是）")
    private byte checkdaliyOrder;

    @ApiModelProperty(name = "printBarcode", value = "打印条码（0-否 1-是）")
    private byte printBarcode;

    @ApiModelProperty(name = "palletType", value = "栈板类型（0-同一工单包栈板 1-同一料号包栈板 2-同PO号包栈板 3-同销售订单作业）")
    private byte palletType;

    @ApiModelProperty(name = "printName", value = "打印机名称")
    private String printName;

    @ApiModelProperty(name="stackingCode",value = "堆垛编码")
    private String stackingCode;

    @ApiModelProperty(name = "isReadHead", value = "是否读头")
    private Boolean isReadHead;

}
