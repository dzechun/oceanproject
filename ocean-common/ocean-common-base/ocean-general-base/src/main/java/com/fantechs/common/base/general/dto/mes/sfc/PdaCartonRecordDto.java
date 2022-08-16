package com.fantechs.common.base.general.dto.mes.sfc;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdaCartonRecordDto implements Serializable {

    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    private String workOrderCode;

    @ApiModelProperty(name="productionQty",value = "投产数量")
    private BigDecimal productionQty;

    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    private BigDecimal workOrderQty;

    @ApiModelProperty(name = "cartonCode", value = "包箱号")
    private String cartonCode;

    @ApiModelProperty(name = "productCartonId", value = "包箱状态表ID")
    private Long productCartonId;

    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    @ApiModelProperty(name="materialDesc",value = "料号描述")
    private String materialDesc;

    @ApiModelProperty(name="packedNum",value = "已包箱数")
    private Integer packedNum;

    @ApiModelProperty(name="scansNum",value = "扫描数")
    private Integer scansNum;

    @ApiModelProperty(name = "cartonNum", value = "包箱规格", required = true)
    private BigDecimal cartonNum;

    @ApiModelProperty(name = "barcodeProcessList", value = "条码列表", required = true)
    private List<MesSfcBarcodeProcessDto> barcodeProcessList;

}
