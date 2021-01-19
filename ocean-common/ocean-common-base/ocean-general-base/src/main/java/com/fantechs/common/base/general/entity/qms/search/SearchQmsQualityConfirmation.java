package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsQualityConfirmation extends BaseQuery implements Serializable {
    /**
     * 品质确认ID
     */
    @ApiModelProperty(name="qualityConfirmationId",value = "品质确认ID")
    private Long qualityConfirmationId;

    /**
     * 品质确认单号
     */
    @ApiModelProperty(name="qualityConfirmationCode",value = "品质确认单号")
    private String qualityConfirmationCode;

    /**
     * 流程单号
     */
    @ApiModelProperty(name="workOrderCardPoolCode",value = "流程单号")
    private String workOrderCardPoolCode;

    /**
     * 父流程单号
     */
    @ApiModelProperty(name="parentWorkOrderCardPoolCode",value = "父流程单号")
    private String parentWorkOrderCardPoolCode;

    /**
     * 工单单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单单号")
    private String workOrderCode;

    /**
     * 品质确认状态（0、待确认 1、确认中 2、已确认）
     */
    @ApiModelProperty(name="affirmStatus",value = "品质确认状态（0、待确认 1、确认中 2、已确认）")
    private Byte affirmStatus;

    /**
     * 品质类型（0、品质确认 1、品质抽检 ）
     */
    @ApiModelProperty(name="qualityType",value = "品质类型（0、品质确认 1、品质抽检 ）")
    private Byte qualityType;

    private static final long serialVersionUID = 1L;
}
