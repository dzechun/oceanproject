package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEngContractQtyOrder extends BaseQuery implements Serializable {

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    private String contractCode;

    /**
     * 专业
     */
    @ApiModelProperty(name="professionName",value = "专业")
    private String professionName;

    /**
     * 装置码
     */
    @ApiModelProperty(name="deviceCode",value = "装置码")
    private String deviceCode;

    /**
     * 主项号
     */
    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    private String dominantTermCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    private String locationNum;

    /**
     * 材料编码
     */
    @ApiModelProperty(name="materialCode",value = "材料编码")
    private String materialCode;

    /**
     * 材料用途
     */
    @ApiModelProperty(name="materialPurpose",value = "材料用途")
    private String materialPurpose;
}
