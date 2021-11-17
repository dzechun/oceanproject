package com.fantechs.common.base.general.dto.esop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EsopWorkInstructionTreeDto implements Serializable {

    /**
     * WI序号
     */
    @ApiModelProperty(name="workInstructionSeqNums",value = "WI序号")
    private String workInstructionSeqNums;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="productModelName",value = "产品名称")
    private String productModelName;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelCode",value = "产品型号")
    private String productModelCode;

    /**
     * 产品规格
     */
    @ApiModelProperty(name="productModelDesc",value = "产品规格")
    private String productModelDesc;


 //   private List<EsopWorkInstructionDto> esopWorkInstructionDtos;

}
