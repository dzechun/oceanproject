package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdaPackingVo implements Serializable {

    @ApiModelProperty(name = "barCode", value = "附件码", required = true)
    private String barCode;
    @ApiModelProperty(name = "stationId", value = "工位ID", required = true)
    private Long stationId;
    @ApiModelProperty(name = "checkOrNot", value = "是否检查排程", required = true)
    private Boolean checkOrNot;
    @ApiModelProperty(name = "annex", value = "扫描附件码", required = true)
    private Boolean annex;
    @ApiModelProperty(name = "print", value = "打印条码", required = true)
    private Boolean print;
    @ApiModelProperty(name = "packType", value = "包箱类型(1：工单包箱，2：料号包箱)", required = true)
    private String packType;
    @ApiModelProperty(name = "num", value = "包箱规格", required = true)
    private Integer num;
    @ApiModelProperty(name = "workOrderId", value = "工单ID(若有则必填)")
    private Long workOrderId;
    @ApiModelProperty(name = "cartonCode", value = "包箱号(若有则必填)")
    private String cartonCode;
}
