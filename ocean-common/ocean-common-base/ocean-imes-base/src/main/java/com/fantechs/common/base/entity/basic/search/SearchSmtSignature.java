package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtSignature extends BaseQuery implements Serializable {

    /**
     * 特征码
     */
    @ApiModelProperty(name="signatureCode" ,value="特征码")
    private String signatureCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;
}
