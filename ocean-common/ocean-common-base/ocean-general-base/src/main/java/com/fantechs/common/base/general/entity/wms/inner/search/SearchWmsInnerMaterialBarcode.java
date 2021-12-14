package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SearchWmsInnerMaterialBarcode extends BaseQuery implements Serializable {

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
     * 来料条码ID
     */
    @ApiModelProperty(name="printOrderDetId",value = "来料条码ID")
    private Long printOrderDetId;

    /**
     * 条码
     */
    @ApiModelProperty(name="printOrderCode",value = "条码")
    private String printOrderCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 来料条码ID列表
     */
    @ApiModelProperty(name="materialBarcodeIdList",value = "来料条码ID列表")
    private List<Long> materialBarcodeIdList;

    private static final long serialVersionUID = 1L;
}
