package com.fantechs.common.base.general.entity.wms.out.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 采购退货单
 * wms_out_ht_purchase_return
 * @author hyc
 * @date 2020-12-31 09:54:52
 */
@Data
@Table(name = "wms_out_ht_purchase_return")
public class WmsOutHtPurchaseReturn implements Serializable {
    @Id
    @Column(name = "ht_purchase_return_id")
    private Long htPurchaseReturnId;

    /**
     * 采购退货单ID
     */
    @ApiModelProperty(name="purchaseReturnId",value = "采购退货单ID")
    @Excel(name = "采购退货单ID", height = 20, width = 30) 
    @Column(name = "purchase_return_id")
    private Long purchaseReturnId;

    /**
     * 采购退货单号
     */
    @ApiModelProperty(name="purchaseReturnCode",value = "采购退货单号")
    @Excel(name = "采购退货单号", height = 20, width = 30) 
    @Column(name = "purchase_return_code")
    private String purchaseReturnCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="customerId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30) 
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 采购员ID
     */
    @ApiModelProperty(name="buyerId",value = "采购员ID")
    @Excel(name = "采购员ID", height = 20, width = 30) 
    @Column(name = "buyer_id")
    private Long buyerId;

    /**
     * 退货原因
     */
    @ApiModelProperty(name="returnType",value = "退货原因")
    @Excel(name = "退货原因", height = 20, width = 30) 
    @Column(name = "return_type")
    private String returnType;

    /**
     * 预计出库时间
     */
    @ApiModelProperty(name="planOuttime",value = "预计出库时间")
    @Excel(name = "预计出库时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_outtime")
    private Date planOuttime;

    /**
     * 实际出库时间
     */
    @ApiModelProperty(name="realityOuttime",value = "实际出库时间")
    @Excel(name = "实际出库时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "reality_outtime")
    private Date realityOuttime;

    /**
     * 单据状态（0-待退货 1-退货中 2-退货完成）
     */
    @ApiModelProperty(name="returnStatus",value = "单据状态（0-待退货 1-退货中 2-退货完成）")
    @Excel(name = "单据状态（0-待退货 1-退货中 2-退货完成）", height = 20, width = 30) 
    @Column(name = "return_status")
    private Byte returnStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30) 
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30) 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30) 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30) 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 供应商代码
     */
    @ApiModelProperty(name="supplierCode" ,value="供应商代码")
    @Excel(name = "供应商代码", height = 20, width = 30,orderNum="2")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName" ,value="供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="3")
    private String supplierName;

    /**
     * 采购员名称
     */
    @ApiModelProperty(name="buyName" ,value="采购员名称")
    @Excel(name = "采购员名称", height = 20, width = 30,orderNum="4")
    private String buyName;

    private static final long serialVersionUID = 1L;
}