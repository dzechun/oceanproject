package com.fantechs.common.base.general.dto.qms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @date 2020-12-28 15:15:07
 */
@Data
public class PdaIncomingDetSubmitDto implements Serializable {

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
