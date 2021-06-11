package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSapProductBomApi implements Serializable {

    /**
     * 开始时间
     */
    @ApiModelProperty(name="materialCode" ,value="成品物料号（必填）")
    private String materialCode;

    @ApiModelProperty(name="oitxt" ,value="说明")
    private String oitxt;
}
