package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloseCartonDto implements Serializable {

    @ApiModelProperty(name = "stationId", value = "工位ID", required = true)
    private Long stationId;
    @ApiModelProperty(name = "processId", value = "工序ID", required = true)
    private Long processId;
/*
    @ApiModelProperty(name = "annex", value = "扫描附件码", required = true)
    private Boolean annex;*/
    @ApiModelProperty(name = "print", value = "打印条码", required = true)
    private Boolean print;
    @ApiModelProperty(name = "productCartonId", value = "包箱状态表ID")
    private Long productCartonId;
    @ApiModelProperty(name = "printName", value = "打印机名称")
    private String printName;

}
