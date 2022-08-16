package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Data
public class SearchBarcodeTrace extends BaseQuery {

    /**
     * 离散任务号
     */
    @ApiModelProperty(name="workOrderCode",value = "离散任务号")
    private String workOrderCode;

    /**
     * 销售单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售单号")
    private String salesOrderCode;

    /**
     * 厂内码
     */
    @ApiModelProperty(name="barcode",value = "厂内码")
    private String barcode;

    /**
     * 销售码
     */
    @ApiModelProperty(name="partBarcode",value = "销售码")
    private String partBarcode;

    /**
     * 流程节点
     */
    @ApiModelProperty(name="prossPoint",value = "流程节点")
    private String prossPoint;

    /**
     * 条码状态
     */
    @ApiModelProperty(name="barcodeStatus",value = "条码状态")
    private String barcodeStatus;

}
