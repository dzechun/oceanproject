package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdaCartonDto implements Serializable {

    @ApiModelProperty(name = "barCode", value = "附件码", required = true)
    private String barCode;
    @ApiModelProperty(name = "stationId", value = "工位ID", required = true)
    private Long stationId;
    @ApiModelProperty(name = "processId", value = "工序ID", required = true)
    private Long processId;
    @ApiModelProperty(name = "checkOrNot", value = "是否检查排程", required = true)
    private Boolean checkOrNot;
    @ApiModelProperty(name = "annex", value = "扫描附件码", required = true)
    private Boolean annex;
    @ApiModelProperty(name = "print", value = "打印条码", required = true)
    private Boolean print;
    @ApiModelProperty(name = "packType", value = "包箱类型(1：工单包箱，2：料号包箱)", required = true)
    private String packType;
    @ApiModelProperty(name = "cartonNum", value = "包箱规格", required = true)
    private Integer cartonNum;
    @ApiModelProperty(name = "closeOrNot", value = "是否关箱")
    private Boolean closeOrNot;
    @ApiModelProperty(name = "workOrderId", value = "工单ID(若有则必填)")
    private Long workOrderId;
    @ApiModelProperty(name = "cartonCode", value = "包箱号(若有则必填)")
    private String cartonCode;
    @ApiModelProperty(name = "productCartonId", value = "包箱状态表ID(若有则必填,若未满箱提交，则必填)")
    private Long productCartonId;
}
