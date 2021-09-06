package com.fantechs.common.base.general.entity.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

import javax.persistence.Table;

/**
 * 请购单
 * eng_purchase_req_order
 * @author Dylan
 * @date 2021-09-02 11:17:46
 */
@Data
@Table(name = "eng_purchase_req_order")
public class EngPurchaseReqOrder extends ValidGroup implements Serializable {
    /**
     * 请购单ID
     */
    @ApiModelProperty(name="purchaseReqOrderId",value = "请购单ID")
    @Excel(name = "请购单ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "purchase_req_order_id")
    private Long purchaseReqOrderId;

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
     * 材料等级
     */
    @ApiModelProperty(name="materialGrade",value = "材料等级")
    @Excel(name = "材料等级", height = 20, width = 30,orderNum="") 
    @Column(name = "material_grade")
    private String materialGrade;

    /**
     * 材料编码
     */
    @ApiModelProperty(name="materialCode",value = "材料编码")
    @Excel(name = "材料编码", height = 20, width = 30,orderNum="") 
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="") 
    @Column(name = "location_num")
    private String locationNum;

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
     * 请购说明
     */
    @ApiModelProperty(name="purchaseReqExplain",value = "请购说明")
    @Excel(name = "请购说明", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_req_explain")
    private String purchaseReqExplain;

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
     * 材料用途
     */
    @ApiModelProperty(name="materialPurpose",value = "材料用途")
    @Excel(name = "材料用途", height = 20, width = 30,orderNum="") 
    @Column(name = "material_purpose")
    private String materialPurpose;

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
     * 采购说明
     */
    @ApiModelProperty(name="purchaseExplain",value = "采购说明")
    @Excel(name = "采购说明", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_explain")
    private String purchaseExplain;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * option1
     */
    @ApiModelProperty(name="option1",value = "option1")
    @Excel(name = "option1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * option2
     */
    @ApiModelProperty(name="option2",value = "option2")
    @Excel(name = "option2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * option3
     */
    @ApiModelProperty(name="option3",value = "option3")
    @Excel(name = "option3", height = 20, width = 30,orderNum="") 
    private String option3;

    private static final long serialVersionUID = 1L;
}