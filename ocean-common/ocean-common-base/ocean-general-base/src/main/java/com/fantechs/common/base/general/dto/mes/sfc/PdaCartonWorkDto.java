package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdaCartonWorkDto implements Serializable {

    @ApiModelProperty(name = "barCode", value = "条码", required = true)
    private String barCode;
    @ApiModelProperty(name = "barAnnexCode", value = "附件码", required = true)
    private String barAnnexCode;
    @ApiModelProperty(name = "stationId", value = "工位ID", required = true)
    private Long stationId;
    @ApiModelProperty(name = "processId", value = "工序ID", required = true)
    private Long processId;
    @ApiModelProperty(name = "proLineId", value = "产线ID", required = true)
    private Long proLineId;
    @ApiModelProperty(name = "checkOrNot", value = "是否检查排程", required = true)
    private Boolean checkOrNot;
    @ApiModelProperty(name = "annex", value = "是否扫描附件码", required = true)
    private Boolean annex;
    @ApiModelProperty(name = "print", value = "是否打印条码", required = true)
    private Boolean print;
    @ApiModelProperty(name = "packType", value = "包箱类型(1：工单包箱，2：料号包箱)", required = true)
    private String packType;
    @ApiModelProperty(name = "printName", value = "打印机名称")
    private String printName;
    @ApiModelProperty(name = "isReadHead", value = "是否读头")
    private Boolean isReadHead;
}
