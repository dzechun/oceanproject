package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/2
 */
@Data
public class SearchWmsInnerInventoryDet extends BaseQuery implements Serializable {
    @ApiModelProperty("库位id")
    private Long storageId;
    @ApiModelProperty("物料id")
    private Long materialId;
    @ApiModelProperty("条码")
    private String barcode;
    @ApiModelProperty("相关单号")
    private String relevanceOrderCode;
    @ApiModelProperty("物料数量")
    private BigDecimal materialQty;
    @ApiModelProperty("是否是不相等 -- 0、相等 1、不相等")
    private Integer notEqualMark;

    @ApiModelProperty("作业状态")
    private Byte jobStatus;

    @ApiModelProperty("物料名称")
    private String materialName;

    private String materialCode;

    private String warehouseName;

    private String supplierName;

    private String asnCode;

    private Byte ifStockLock;

    private Byte lockStatus;

    private String productionBatchCode;

    private String createUserName;
    private String modifiedUserName;

    @ApiModelProperty("库位")
    private String storageCode;

    @ApiModelProperty("库存状态id")
    private Long inventoryStatusId;

    @ApiModelProperty("检验单编码")
    private String inspectionOrderCode;

    @ApiModelProperty("检验单编码是否为空  0-否 1-是")
    private Integer ifInspectionOrderCodeNull;

    @ApiModelProperty(name = "deliveryOrderCode",value = "发货单号")
    private String deliveryOrderCode;

    @ApiModelProperty(name = "barcodeStatus",value = "条码状态")
    private String barcodeStatus;

    @ApiModelProperty(name="qcDate",value = "质检日期")
    private String qcDate;

    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;

    // 20220104 bgkun

    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String salesBarcode;

    @ApiModelProperty(name = "customerBarcode",value = "客户条码")
    private String customerBarcode;

    /**
     * ERP逻辑仓编码
     */
    @ApiModelProperty(name="logicCode",value = "ERP逻辑仓编码")
    private String logicCode;

    @ApiModelProperty("库位类型 1 存货 2 收货 3 发货")
    private Byte storageType;


}
