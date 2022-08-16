package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSapProductBomApi implements Serializable {


    @ApiModelProperty(name="materialCode" ,value="成品物料号（必填）")
    private String materialCode;

    @ApiModelProperty(name="oitxt" ,value="对象管理记录描述")
    private String oitxt;
}
