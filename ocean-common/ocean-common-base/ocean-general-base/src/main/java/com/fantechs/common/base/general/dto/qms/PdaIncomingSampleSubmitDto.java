package com.fantechs.common.base.general.dto.qms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


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
     * 来料检验单明细不良类别列表
     */
    @ApiModelProperty(name="pdaIncomingDetSubmitDtoList",value = "来料检验单明细不良类别列表")
    private List<PdaIncomingDetSubmitDto> pdaIncomingDetSubmitDtoList;

}
