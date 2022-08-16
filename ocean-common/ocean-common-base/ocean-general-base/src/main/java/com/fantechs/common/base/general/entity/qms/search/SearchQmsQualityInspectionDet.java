package com.fantechs.common.base.general.entity.qms.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2020/12/16 11:32
 */
@Data
public class SearchQmsQualityInspectionDet extends BaseQuery implements Serializable {

    /**
     * 质检单号
     */
    @ApiModelProperty(name="qualityInspectionCode",value = "质检单号")
    private String qualityInspectionCode;

    /**
     * 检验状态（0-未检验 1-检验中 2-已检验）
     */
    @ApiModelProperty(name="checkoutStatus",value = "单据状态（0-未检验 1-检验中 2-已检验）")
    private Byte checkoutStatus;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;


}
