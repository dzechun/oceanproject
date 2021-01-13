package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;

/**
 * 采购退货单
 * wms_out_purchase_return
 * @author hyc
 * @date 2020-12-24 09:37:49
 */
@Data
@Table(name = "wms_out_purchase_return")
public class WmsOutPurchaseReturn extends ValidGroup implements Serializable {
    /**
     * 采购退货单ID
     */
    @ApiModelProperty(name="purchaseReturnId",value = "采购退货单ID")
    @NotNull(groups = update.class,message = "成品出库明细单ID不能为空")
    @Id
    @Column(name = "purchase_return_id")
    private Long purchaseReturnId;

    /**
     * 采购退货单号
     */
    @ApiModelProperty(name="purchaseReturnCode",value = "采购退货单号")
    @Excel(name = "采购退货单号", height = 20, width = 30,orderNum="1")
    @Column(name = "purchase_return_code")
    private String purchaseReturnCode;

    /**
     * 供应商Id
     */
    @ApiModelProperty(name="supplierId",value = "供应商Id")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 采购员ID
     */
    @ApiModelProperty(name="buyerId",value = "采购员ID")
    @Column(name = "buyer_id")
    private Long buyerId;

    /**
     * 退货原因
     */
    @ApiModelProperty(name="returnType",value = "退货原因")
    @Excel(name = "退货原因", height = 20, width = 30,orderNum="5")
    @Column(name = "return_type")
    private String returnType;

    /**
     * 预计出库时间
     */
    @ApiModelProperty(name="planOuttime",value = "预计出库时间")
    @Excel(name = "预计出库时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_outtime")
    private Date planOuttime;

    /**
     * 实际出库时间
     */
    @ApiModelProperty(name="realityOuttime",value = "实际出库时间")
    @Excel(name = "实际出库时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "reality_outtime")
    private Date realityOuttime;

    /**
     * 单据状态（0-待退货 1-退货中 2-退货完成）
     */
    @ApiModelProperty(name="returnStatus",value = "单据状态（0-待退货 1-退货中 2-退货完成）")
    @Excel(name = "单据状态（0-待退货 1-退货中 2-退货完成）", height = 20, width = 30,orderNum="8")
    @Column(name = "return_status")
    private Byte returnStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    @ApiModelProperty(name="wmsOutPurchaseReturnDetList",value = "子表明细")
    private List<WmsOutPurchaseReturnDet> wmsOutPurchaseReturnDetList;

    private static final long serialVersionUID = 1L;
}