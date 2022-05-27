package com.fantechs.common.base.general.dto.wanbao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WanbaoAutoStackingListDto implements Serializable {

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @ApiModelProperty(name="stackingId",value = "堆码号ID")
    private Long stackingId;

    @ApiModelProperty(name="stackingCode",value = "堆垛编码")
    private String stackingCode;

    @ApiModelProperty(name="stackingName",value = "堆垛名称")
    private String stackingName;

    @ApiModelProperty(name="count",value = "已扫描箱数")
    private int count;

    @ApiModelProperty(name="list",value = "条码集合")
    private List<WanbaoAutoStackingDto> list;

}
