package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseSignature extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -2677190668550208596L;
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
