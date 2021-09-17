package com.fantechs.common.base.general.dto.eng;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
public class EngContractQtyOrderAndPurOrderDto implements Serializable {
    /**
     * 合同量单ID
     */
    @Transient
    @ApiModelProperty(name="contractQtyOrderId",value = "合同量单ID")
    @Id
    private Long contractQtyOrderId;

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
     * 供应商ID
     */
    @Transient
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 请购说明
     */
    @Transient
    @ApiModelProperty(name="purchaseReqExplain",value = "请购说明")
    private String purchaseReqExplain;

    /**
     * 采购说明
     */
    @Transient
    @ApiModelProperty(name="purchaseExplain",value = "采购说明")
    private String purchaseExplain;

    /**
     * 备注
     */
    @Transient
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;


    /**
     * 状态(0无效，1有效)
     */
    @Transient
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;


    /**
     * 组织id
     */
    @Transient
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @Transient
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @Transient
    @ApiModelProperty(name="createTime",value = "创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Transient
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Transient
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * option1
     */
    @Transient
    @ApiModelProperty(name="option1",value = "option1")
    private String option1;

    /**
     * option2
     */
    @Transient
    @ApiModelProperty(name="option2",value = "option2")
    private String option2;

    /**
     * option3
     */
    @Transient
    @ApiModelProperty(name="option3",value = "option3")
    private String option3;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;


}
