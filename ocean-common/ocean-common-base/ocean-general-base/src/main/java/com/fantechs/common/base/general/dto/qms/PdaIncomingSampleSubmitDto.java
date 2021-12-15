package com.fantechs.common.base.general.dto.qms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @date 2020-12-28 15:15:07
 */
@Data
public class PdaIncomingSampleSubmitDto implements Serializable {

    /**
     * 产品条码
     */
    @ApiModelProperty(name="barcode",value = "产品条码")
    private String barcode;

    /**
     * 条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "条码ID")
    private Long materialBarcodeId;

    /**
     * 样本值
     */
    @ApiModelProperty(name="sampleValue",value = "样本值")
    private String sampleValue;

    /**
     * 不良现象ID
     */
    @ApiModelProperty(name="badnessPhenotypeId",value = "不良现象ID")
    private Long badnessPhenotypeId;


    /**
     * 来料检验单明细id
     */
    @ApiModelProperty(name="incomingInspectionOrderDetId",value = "来料检验单明细id")
    private Long incomingInspectionOrderDetId;

    /**
     * 不良类别ID
     */
    @ApiModelProperty(name="badnessCategoryId",value = "不良类别ID")
    private Long badnessCategoryId;


}
