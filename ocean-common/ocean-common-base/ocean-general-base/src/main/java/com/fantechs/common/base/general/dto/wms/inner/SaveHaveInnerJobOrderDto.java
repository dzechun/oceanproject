package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
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
     * 条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)
     */
    @ApiModelProperty(name="barcodeType",value = "条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)")
    private Byte barcodeType;

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
    private String productionTime;

    /**
     * 是否系统条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifSysBarcode",value = "是否系统条码(0-否 1-是)")
    private String ifSysBarcode;



}
