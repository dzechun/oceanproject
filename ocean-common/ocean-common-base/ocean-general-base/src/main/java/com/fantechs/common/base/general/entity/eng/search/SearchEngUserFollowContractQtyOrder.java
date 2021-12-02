package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchEngUserFollowContractQtyOrder extends BaseQuery implements Serializable {

    /**
     * 合同量单ID
     */
    @ApiModelProperty(name = "contractQtyOrderId",value = "合同量单ID")
    private Long contractQtyOrderId;

    /**
     * 合同量单ID列表
     */
    @ApiModelProperty(name = "contractQtyOrderIds",value = "合同量单ID列表")
    private List<Long> contractQtyOrderIds;

    /**
     * 用户ID
     */
    @ApiModelProperty(name = "userId",value = "用户ID")
    private Long userId;

    /**
     * 请购单号
     */
    @Transient
    @ApiModelProperty(name="purchaseReqOrderCode",value = "请购单号")
    private String purchaseReqOrderCode;

    /**
     * 请购单名称
     */
    @Transient
    @ApiModelProperty(name="purchaseReqOrderName",value = "请购单名称")
    private String purchaseReqOrderName;

    /**
     * 合同号
     */
    @Transient
    @ApiModelProperty(name="contractCode",value = "合同号")
    private String contractCode;

    /**
     * 专业编码
     */
    @Transient
    @ApiModelProperty(name="professionCode",value = "专业编码")
    private String professionCode;

    /**
     * 专业名称
     */
    @Transient
    @ApiModelProperty(name="professionName",value = "专业名称")
    private String professionName;

    /**
     * 材料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "材料编码")
    private String materialCode;

    /**
     * 材料名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "材料名称")
    private String materialName;

    /**
     * 设计量
     */
    @Transient
    @ApiModelProperty(name="designQty",value = "设计量")
    private String designQty;

    /**
     * 余量
     */
    @Transient
    @ApiModelProperty(name="surplusQty",value = "余量")
    private String surplusQty;

    /**
     * 请购量
     */
    @Transient
    @ApiModelProperty(name="purchaseReqQty",value = "请购量")
    private String purchaseReqQty;

    /**
     * 采购量
     */
    @Transient
    @ApiModelProperty(name="purQty",value = "采购量")
    private String purQty;

    /**
     * 材料等级
     */
    @Transient
    @ApiModelProperty(name="materialGrade",value = "材料等级")
    private String materialGrade;

    /**
     * 材料用途
     */
    @Transient
    @ApiModelProperty(name="materialPurpose",value = "材料用途")
    private String materialPurpose;

    /**
     * 位号
     */
    @Transient
    @ApiModelProperty(name="locationNum",value = "位号")
    private String locationNum;

    /**
     * 装置码
     */
    @Transient
    @ApiModelProperty(name="deviceCode",value = "装置码")
    private String deviceCode;

    /**
     * 主项号
     */
    @Transient
    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    private String dominantTermCode;

    /**
     * 备注
     */
    @Transient
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;
}
