package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SaveHaveInnerJobOrderDto implements Serializable {

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    private Long storageId;

    /**
     * 作业单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "作业单明细ID")
    private Long jobOrderDetId;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    private Long materialBarcodeId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    /**
     * 条码物料数量
     */
    @ApiModelProperty(name="materialQty",value = "条码物料数量")
    private BigDecimal materialQty;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionTime",value = "生产日期")
    private Date productionTime;

    /**
     * 是否系统条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifSysBarcode",value = "是否系统条码(0-否 1-是)")
    private String ifSysBarcode;



}
