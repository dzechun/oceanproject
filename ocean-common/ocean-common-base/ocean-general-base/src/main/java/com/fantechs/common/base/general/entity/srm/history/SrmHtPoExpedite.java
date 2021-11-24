package com.fantechs.common.base.general.entity.srm.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.srm.SrmPoExpediteRecord;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;
;

/**
 * 订单跟催履历表
 * srm_ht_po_expedite
 * @author jbb
 * @date 2021-11-18 11:41:03
 */
@Data
@Table(name = "srm_ht_po_expedite")
public class SrmHtPoExpedite extends ValidGroup implements Serializable {
    /**
     * 订单跟催履历ID
     */
    @ApiModelProperty(name="htPoExpediteId",value = "订单跟催履历ID")
    @Excel(name = "订单跟催履历ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_po_expedite_id")
    private Long htPoExpediteId;

    /**
     * 订单跟催ID
     */
    @ApiModelProperty(name="poExpediteId",value = "订单跟催ID")
    @Excel(name = "订单跟催ID", height = 20, width = 30,orderNum="")
    @Column(name = "po_expedite_id")
    private Long poExpediteId;

    /**
     * 采购订单ID
     */
    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    @Excel(name = "采购订单ID", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="")
    @Column(name = "supplier_id")
    private Long supplierId;

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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 明细集合
     */
    @Transient
    @ApiModelProperty(name="list",value = "明细集合")
    private List<SrmHtPoExpediteRecord> list;


    /**
     * 采购订单号
     */
    @Transient
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    @Excel(name = "采购订单号", height = 20, width = 30,orderNum="1")
    private String purchaseOrderCode;

    /**
     * 单据状态
     */
    @Transient
    @ApiModelProperty(name="orderStatus",value = "单据状态")
    @Excel(name = "单据状态", height = 20, width = 30,orderNum="2")
    private Byte orderStatus;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="3")
    private String supplierName;

    /**
     * 订单日期
     */
    @Transient
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="4",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date orderDate;

    /**
     * 采购部门
     */
    @Transient
    @ApiModelProperty(name="purchaseDeptName",value = "采购部门")
    @Excel(name = "采购部门", height = 20, width = 30,orderNum="5")
    private String purchaseDeptName;

    /**
     * 制单人
     */
    @Transient
    @ApiModelProperty(name="makeOrderUserName",value = "制单人")
    @Excel(name = "制单人", height = 20, width = 30,orderNum="6")
    private String makeOrderUserName;

    /**
     * 制单日期
     */
    @Transient
    @ApiModelProperty(name="makeOrderDate",value = "制单日期")
    @Excel(name = "制单日期", height = 20, width = 30,orderNum="7")
    private Date makeOrderDate;

    /**
     * 备注说明
     */
    @Transient
    @ApiModelProperty(name="orderRemark",value = "备注说明")
    @Excel(name = "创建用备注说明户名称", height = 20, width = 30,orderNum="8")
    private String orderRemark;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}
