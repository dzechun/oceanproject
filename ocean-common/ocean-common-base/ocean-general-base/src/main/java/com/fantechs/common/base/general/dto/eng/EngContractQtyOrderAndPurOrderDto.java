package com.fantechs.common.base.general.dto.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class EngContractQtyOrderAndPurOrderDto implements Serializable {
    /**
     * 合同量单ID
     */
    @Id
    @ApiModelProperty(name="contractQtyOrderId",value = "合同量单ID")
    @Excel(name = "合同量单ID", height = 20, width = 30,orderNum="")
    @Column(name = "contract_qty_order_id")
    private Long contractQtyOrderId;

    /**
     * 请购单号
     */
    @ApiModelProperty(name="purchaseReqOrderCode",value = "请购单号")
    @Excel(name = "请购单号", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_req_order_code")
    private String purchaseReqOrderCode;

    /**
     * 请购单名称
     */
    
    @ApiModelProperty(name="purchaseReqOrderName",value = "请购单名称")
    @Excel(name = "请购单名称", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_req_order_name")
    private String purchaseReqOrderName;

    /**
     * 合同号
     */
    
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="")
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 专业编码
     */
    
    @ApiModelProperty(name="professionCode",value = "专业编码")
    @Excel(name = "专业编码", height = 20, width = 30,orderNum="")
    @Column(name = "profession_code")
    private String professionCode;

    /**
     * 专业名称
     */
    
    @ApiModelProperty(name="professionName",value = "专业名称")
    @Excel(name = "专业名称", height = 20, width = 30,orderNum="")
    @Column(name = "profession_name")
    private String professionName;

    /**
     * 材料编码
     */
    
    @ApiModelProperty(name="materialCode",value = "材料编码")
    @Excel(name = "材料编码", height = 20, width = 30,orderNum="")
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 材料名称
     */
    
    @ApiModelProperty(name="materialName",value = "材料名称")
    @Excel(name = "材料名称", height = 20, width = 30,orderNum="")
    @Column(name = "material_name")
    private String materialName;

    /**
     * 设计量
     */
    
    @ApiModelProperty(name="designQty",value = "设计量")
    @Excel(name = "设计量", height = 20, width = 30,orderNum="")
    @Column(name = "design_qty")
    private String designQty;

    /**
     * 余量
     */
    
    @ApiModelProperty(name="surplusQty",value = "余量")
    @Excel(name = "余量", height = 20, width = 30,orderNum="")
    @Column(name = "surplus_qty")
    private String surplusQty;

    /**
     * 请购量
     */
    
    @ApiModelProperty(name="purchaseReqQty",value = "请购量")
    @Excel(name = "请购量", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_req_qty")
    private String purchaseReqQty;

    /**
     * 采购量
     */
    
    @ApiModelProperty(name="purQty",value = "采购量")
    @Excel(name = "采购量", height = 20, width = 30,orderNum="")
    @Column(name = "pur_qty")
    private String purQty;

    /**
     * 材料等级
     */
    
    @ApiModelProperty(name="materialGrade",value = "材料等级")
    @Excel(name = "材料等级", height = 20, width = 30,orderNum="")
    @Column(name = "material_grade")
    private String materialGrade;

    /**
     * 材料用途
     */
    
    @ApiModelProperty(name="materialPurpose",value = "材料用途")
    @Excel(name = "材料用途", height = 20, width = 30,orderNum="")
    @Column(name = "material_purpose")
    private String materialPurpose;

    /**
     * 位号
     */
    
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="")
    @Column(name = "location_num")
    private String locationNum;

    /**
     * 装置码
     */
    
    @ApiModelProperty(name="deviceCode",value = "装置码")
    @Excel(name = "装置码", height = 20, width = 30,orderNum="")
    @Column(name = "device_code")
    private String deviceCode;

    /**
     * 主项号
     */
    
    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    @Excel(name = "主项号", height = 20, width = 30,orderNum="")
    @Column(name = "dominant_term_code")
    private String dominantTermCode;

    /**
     * 供应商ID
     */
    
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 供应商名称
     */
    
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="")
    @Column(name = "supplier_name")
    private String supplierName;

    /**
     * 请购说明
     */
    
    @ApiModelProperty(name="purchaseReqExplain",value = "请购说明")
    @Excel(name = "请购说明", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_req_explain")
    private String purchaseReqExplain;

    /**
     * 采购说明
     */
    
    @ApiModelProperty(name="purchaseExplain",value = "采购说明")
    @Excel(name = "采购说明", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_explain")
    private String purchaseExplain;

    /**
     * 备注
     */
    
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    @Column(name = "remark")
    private String remark;


    /**
     * 状态(0无效，1有效)
     */
    
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Column(name = "status")
    private Byte status;


    /**
     * 组织id
     */
    
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建用户名称
     */
    
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Column(name = "create_user_name")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="")
    private String createUserName;

    /**
     * 创建时间
     */
    
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改用户名称
     */
    
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="")
    @Column(name = "modified_user_name")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * option1
     */
    
    @ApiModelProperty(name="option1",value = "option1")
    @Column(name = "option1")
    private String option1;

    /**
     * option2
     */
    
    @ApiModelProperty(name="option2",value = "option2")
    @Column(name = "option2")
    private String option2;

    /**
     * option3
     */
    
    @ApiModelProperty(name="option3",value = "option3")
    @Column(name = "option3")
    private String option3;

    /**
     * 组织名称
     */
    
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Column(name = "organization_name")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
