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
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 采购退货单明细
 * wms_out_purchase_return_det
 * @author hyc
 * @date 2020-12-24 09:37:52
 */
@Data
@Table(name = "wms_out_purchase_return_det")
public class WmsOutPurchaseReturnDet extends ValidGroup implements Serializable {


    /**
     * 采购退货明细单ID
     */
    @ApiModelProperty(name="purchaseReturnDetId",value = "采购退货明细单ID")
    @NotNull(groups = update.class,message = "成品出库明细单ID不能为空")
    @Id
    @Column(name = "purchase_return_det_id")
    private Long purchaseReturnDetId;

    /**
     * 采购退货单ID
     */
    @ApiModelProperty(name="purchaseReturnId",value = "采购退货单ID")
    @Column(name = "purchase_return_id")
    private Long purchaseReturnId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 计划出库数量
     */
    @ApiModelProperty(name="planOutquantity",value = "计划出库数量")
    @Excel(name = "计划出库数量", height = 20, width = 30,orderNum="4")
    @Column(name = "plan_outquantity")
    private BigDecimal planOutquantity;

    /**
     * 实际出库数量
     */
    @ApiModelProperty(name="realityOutquantity",value = "实际出库数量")
    @Excel(name = "实际出库数量", height = 20, width = 30,orderNum="5")
    @Column(name = "reality_outquantity")
    private BigDecimal realityOutquantity;

    /**
     * 应退金额
     */
    @ApiModelProperty(name="planPrice",value = "应退金额")
    @Excel(name = "应退金额", height = 20, width = 30,orderNum="6")
    @Column(name = "plan_price")
    private BigDecimal planPrice;

    /**
     * 实退金额
     */
    @ApiModelProperty(name="realityPrice",value = "实退金额")
    @Excel(name = "实退金额", height = 20, width = 30,orderNum="7")
    @Column(name = "reality_price")
    private BigDecimal realityPrice;

    /**
     * 仓库ID（出货仓库）
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID（出货仓库）")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 仓库管理员ID
     */
    @ApiModelProperty(name="warehouseUserId",value = "仓库管理员ID")
    @Column(name = "warehouse_user_id")
    private Long warehouseUserId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}