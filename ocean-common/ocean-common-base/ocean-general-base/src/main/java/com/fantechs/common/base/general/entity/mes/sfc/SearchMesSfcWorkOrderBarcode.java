package com.fantechs.common.base.general.entity.mes.sfc;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/4/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesSfcWorkOrderBarcode extends BaseQuery implements Serializable {
    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 条码id
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "条码id")
    private Long workOrderBarcodeId;

    /**
     * 条码类别
     */
    @ApiModelProperty(name="labelCategoryId",value = "条码类别")
    private Long labelCategoryId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    private Byte barcodeType;

    private Long orgId;

    /**
     * 条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
     */
    @ApiModelProperty(name = "barcodeStatus",value = "条码状态(0-待投产 1-投产中 2-已完成 3-待打印)")
    private String barcodeStatus;
}
