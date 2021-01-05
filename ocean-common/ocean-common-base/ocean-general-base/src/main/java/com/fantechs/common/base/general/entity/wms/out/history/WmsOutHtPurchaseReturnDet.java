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
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 采购退货单明细
 * wms_out_ht_purchase_return_det
 * @author hyc
 * @date 2020-12-31 09:54:52
 */
@Data
@Table(name = "wms_out_ht_purchase_return_det")
public class WmsOutHtPurchaseReturnDet implements Serializable {
    @Id
    @Column(name = "ht_purchase_return_det_id")
    private Long htPurchaseReturnDetId;

    /**
     * 采购退货明细单ID
     */
    @ApiModelProperty(name="purchaseReturnDetId",value = "采购退货明细单ID")
    @Excel(name = "采购退货明细单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_return_det_id")
    private Long purchaseReturnDetId;

    /**
     * 采购退货单ID
     */
    @ApiModelProperty(name="purchaseReturnId",value = "采购退货单ID")
    @Excel(name = "采购退货单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_return_id")
    private Long purchaseReturnId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 计划出库数量
     */
    @ApiModelProperty(name="planOutquantity",value = "计划出库数量")
    @Excel(name = "计划出库数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_outquantity")
    private BigDecimal planOutquantity;

    /**
     * 实际出库数量
     */
    @ApiModelProperty(name="realityOutquantity",value = "实际出库数量")
    @Excel(name = "实际出库数量", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_outquantity")
    private BigDecimal realityOutquantity;

    /**
     * 应退金额
     */
    @ApiModelProperty(name="planPrice",value = "应退金额")
    @Excel(name = "应退金额", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_price")
    private BigDecimal planPrice;

    /**
     * 实退金额
     */
    @ApiModelProperty(name="realityPrice",value = "实退金额")
    @Excel(name = "实退金额", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_price")
    private BigDecimal realityPrice;

    /**
     * 仓库ID（出货仓库）
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID（出货仓库）")
    @Excel(name = "仓库ID（出货仓库）", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 仓库管理员ID
     */
    @ApiModelProperty(name="warehouseUserId",value = "仓库管理员ID")
    @Excel(name = "仓库管理员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_user_id")
    private Long warehouseUserId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
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
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

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
     * 采购退货单ID
     */
    @ApiModelProperty(name="purchaseReturnCode",value = "采购退货单号")
    @Excel(name = "采购退货单号", height = 20, width = 30,orderNum="1")
    private String purchaseReturnCode;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="8")
    private String warehouseCode;

    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="9")
    private String warehouseName;

    @ApiModelProperty(name="warehouseUserName" ,value="仓库管理员名称")
    @Excel(name = "仓库管理员名称", height = 20, width = 30,orderNum="10")
    private String warehouseUserName;

    private static final long serialVersionUID = 1L;
}