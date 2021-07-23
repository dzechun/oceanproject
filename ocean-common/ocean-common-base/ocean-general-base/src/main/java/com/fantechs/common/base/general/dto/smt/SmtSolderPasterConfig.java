package com.fantechs.common.base.general.dto.smt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author mr.lei
 * @Date 2021/7/22
 */
@Data
public class SmtSolderPasterConfig {

    @ApiModelProperty(name = "solderPasteId",value = "锡膏id")
    private Long solderPasteId;

    @ApiModelProperty(name = "tBackTimeStatus",value = "回温执行状态（0-警告、1-强制执行）")
    private Byte tBackTimeStatus;

    @ApiModelProperty(name = "stirTimeStatus",value = "搅拌时间执行状态（0-警告、1-强制执行）")
    private Byte stirTimeStatus;

    @ApiModelProperty(name = "openTimeLimitStatus",value = "已开封时间执行状态（0-警告、1-强制执行）")
    private Byte openTimeLimitStatus;

    @ApiModelProperty(name = "notOpenTimeLimitStatus",value = "已开封时间执行状态（0-警告、1-强制执行）")
    private Byte notOpenTimeLimitStatus;

    @ApiModelProperty(name = "returnIceTimeStatus",value = "回冰次数执行状态（0-警告、1-强制执行）")
    private Byte returnIceTimeStatus;

    @ApiModelProperty(name = "productionDateStatus",value = "过期日期执行状态（0-警告、1-强制执行）")
    private Byte productionDateStatus;
}
