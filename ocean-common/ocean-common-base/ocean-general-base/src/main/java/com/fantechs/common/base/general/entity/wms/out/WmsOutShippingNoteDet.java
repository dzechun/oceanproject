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
import java.util.List;

;

/**
 * 出货通知单明细
 * wms_out_shipping_note_det
 * @author hyc
 * @date 2021-01-09 15:10:03
 */
@Data
@Table(name = "wms_out_shipping_note_det")
public class WmsOutShippingNoteDet extends ValidGroup implements Serializable {
    /**
     * 出货通知单明细ID
     */
    @ApiModelProperty(name="shippingNoteDetId",value = "出货通知单明细ID")
    @Id
    @Column(name = "shipping_note_det_id")
    @NotNull(groups = update.class,message = "出货通知单明细ID不能为空")
    private Long shippingNoteDetId;

    /**
     * 出货通知单ID
     */
    @ApiModelProperty(name="shippingNoteId",value = "出货通知单ID")
    @Column(name = "shipping_note_id")
    private Long shippingNoteId;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="productModelId",value = "产品ID")
    @Column(name = "product_model_id")
    private Long productModelId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 调入储位ID
     */
    @ApiModelProperty(name="moveStorageId",value = "调入储位ID")
    @Column(name = "move_storage_id")
    private Long moveStorageId;

    /**
     * 计划出货箱数
     */
    @ApiModelProperty(name="planCartonQty",value = "计划出货箱数")
    @Excel(name = "计划出货箱数", height = 20, width = 30,orderNum="10")
    @Column(name = "plan_carton_qty")
    private BigDecimal planCartonQty;

    /**
     * 计划出货总数
     */
    @ApiModelProperty(name="planTotalQty",value = "计划出货总数")
    @Excel(name = "计划出货总数", height = 20, width = 30,orderNum="11")
    @Column(name = "plan_total_qty")
    private BigDecimal planTotalQty;

    /**
     * 备料箱数
     */
    @ApiModelProperty(name="realityCartonQty",value = "备料箱数")
    @Excel(name = "备料箱数", height = 20, width = 30,orderNum="12")
    @Column(name = "reality_carton_qty")
    private BigDecimal realityCartonQty;

    /**
     * 备料总数
     */
    @ApiModelProperty(name="realityTotalQty",value = "备料总数")
    @Excel(name = "备料总数", height = 20, width = 30,orderNum="13")
    @Column(name = "reality_total_qty")
    private BigDecimal realityTotalQty;

    /**
     * 备料状态（0-待备料 1-备料中 2- 备料完成）
     */
    @ApiModelProperty(name="stockStatus",value = "备料状态（0-待备料 1-备料中 2- 备料完成）")
    @Excel(name = "备料状态（0-待备料 1-备料中 2- 备料完成）", height = 20, width = 30,orderNum="14",replace = {"待备料_0","备料中_1","备料完成_2"})
    @Column(name = "stock_status")
    private Byte stockStatus;

    /**
     * 出库状态（0-待出库 1-出库中 2- 出库完成）
     */
    @ApiModelProperty(name="outStatus",value = "出库状态（0-待出库 1-出库中 2- 出库完成）")
    @Excel(name = "出库状态（0-待出库 1-出库中 2- 出库完成）", height = 20, width = 30,orderNum="15", replace = {"待出库_0","出库中_1","出库完成_2"})
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="")
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
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "organization_id")
    private Long organizationId;

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

    @ApiModelProperty(name="stockPalletList",value = "已调拨栈板集合")
    private List<String> stockPalletList;

//    @ApiModelProperty(name="outPalletList",value = "已出库栈板集合")
//    private List<String> outPalletList;

    private static final long serialVersionUID = 1L;
}