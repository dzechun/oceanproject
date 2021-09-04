package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEngPurchaseReqOrder extends BaseQuery implements Serializable {
    /**
     * 请购单号
     */
    @ApiModelProperty(name="purchaseReqOrderCode",value = "请购单号")
    private String purchaseReqOrderCode;

    /**
     * 请购单名称
     */
    @ApiModelProperty(name="purchaseReqOrderName",value = "请购单名称")
    private String purchaseReqOrderName;

    /**
     * 材料等级
     */
    @ApiModelProperty(name="materialGrade",value = "材料等级")
    private String materialGrade;

    /**
     * 材料编码
     */
    @ApiModelProperty(name="materialCode",value = "材料编码")
    private String materialCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    private String locationNum;

    /**
     * 装置码
     */
    @ApiModelProperty(name="deviceCode",value = "装置码")
    private String deviceCode;

    /**
     * 材料用途
     */
    @ApiModelProperty(name="materialPurpose",value = "材料用途")
    private String materialPurpose;

    /**
     * 采购说明
     */
    @ApiModelProperty(name="purchaseExplain",value = "采购说明")
    private String purchaseExplain;
}
