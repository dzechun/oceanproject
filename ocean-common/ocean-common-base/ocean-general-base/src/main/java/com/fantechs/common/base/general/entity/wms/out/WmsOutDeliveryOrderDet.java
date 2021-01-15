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
 * 出库单明细
 * wms_out_delivery_order_det
 * @author hyc
 * @date 2021-01-09 20:01:33
 */
@Data
@Table(name = "wms_out_delivery_order_det")
public class WmsOutDeliveryOrderDet extends ValidGroup implements Serializable {
    /**
     * 出库单明细ID
     */
    @ApiModelProperty(name="deliveryOrderDetId",value = "出库单明细ID")
    @NotNull(groups = update.class,message = "出库单明细ID不能为空")
    @Id
    @Column(name = "delivery_order_det_id")
    private Long deliveryOrderDetId;

    /**
     * 出库单ID
     */
    @ApiModelProperty(name="deliveryOrderId",value = "出库单ID")
    @Excel(name = "出库单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_order_id")
    private Long deliveryOrderId;

    /**
     * 出货通知单明细ID
     */
    @ApiModelProperty(name="shippingNoteDetId",value = "出货通知单明细ID")
    @Excel(name = "出货通知单明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "shipping_note_det_id")
    private Long shippingNoteDetId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 备料总数
     */
    @ApiModelProperty(name="realityTotalQty",value = "备料总数")
    @Excel(name = "备料总数", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_total_qty")
    private BigDecimal realityTotalQty;

    /**
     * 出库总数
     */
    @ApiModelProperty(name="outTotalQty",value = "出库总数")
    @Excel(name = "出库总数", height = 20, width = 30,orderNum="") 
    @Column(name = "out_total_qty")
    private BigDecimal outTotalQty;

    /**
     * 单据状态（0-待出库 1-出库中 2-出库完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待出库 1-出库中 2-出库完成）")
    @Excel(name = "单据状态（0-待出库 1-出库中 2-出库完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "out_status")
    private Byte outStatus;

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


    @ApiModelProperty(name="outPalletList",value = "已出库栈板集合")
    private List<String> outPalletList;

    private static final long serialVersionUID = 1L;
}