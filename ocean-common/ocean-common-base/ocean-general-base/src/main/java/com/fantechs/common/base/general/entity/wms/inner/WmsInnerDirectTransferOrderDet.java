package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 直接调拨单明细表
 * wms_inner_direct_transfer_order_det
 * @author 81947
 * @date 2021-12-21 15:40:10
 */
@Data
@Table(name = "wms_inner_direct_transfer_order_det")
public class WmsInnerDirectTransferOrderDet extends ValidGroup implements Serializable {
    /**
     * 直接调拨单明细ID
     */
    @ApiModelProperty(name="directTransferOrderDetId",value = "直接调拨单明细ID")
    @Excel(name = "直接调拨单明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "direct_transfer_order_det_id")
    private Long directTransferOrderDetId;

    /**
     * 直接调拨单ID
     */
    @ApiModelProperty(name="directTransferOrderId",value = "直接调拨单ID")
    @Excel(name = "直接调拨单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "direct_transfer_order_id")
    private Long directTransferOrderId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单据编码")
    @Excel(name = "核心单据编码", height = 20, width = 30,orderNum="") 
    @Column(name = "core_source_order_code")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据编码")
    @Excel(name = "来源单据编码", height = 20, width = 30,orderNum="") 
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 核心单据明细ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心单据明细ID")
    @Excel(name = "核心单据明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "core_source_id")
    private Long coreSourceId;

    /**
     * 来源明细ID
     */
    @ApiModelProperty(name="sourceId",value = "来源明细ID")
    @Excel(name = "来源明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 行号
     */
    @ApiModelProperty(name="lineNumber",value = "行号")
    @Excel(name = "行号", height = 20, width = 30,orderNum="") 
    @Column(name = "line_number")
    private String lineNumber;

    /**
     * 移入库位ID
     */
    @ApiModelProperty(name="inStorageId",value = "移入库位ID")
    @Excel(name = "移入库位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "in_storage_id")
    private Long inStorageId;

    /**
     * 移出库位ID
     */
    @ApiModelProperty(name="outStorageId",value = "移出库位ID")
    @Excel(name = "移出库位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "out_storage_id")
    private Long outStorageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_qty")
    private BigDecimal planQty;

    /**
     * 实际数量
     */
    @ApiModelProperty(name="actualQty",value = "实际数量")
    @Excel(name = "实际数量", height = 20, width = 30,orderNum="") 
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 行状态(1-待分配、2-待作业、3-完成)
     */
    @ApiModelProperty(name="lineStatus",value = "行状态(1-待分配、2-待作业、3-完成)")
    @Excel(name = "行状态(1-待分配、2-待作业、3-完成)", height = 20, width = 30,orderNum="") 
    @Column(name = "line_status")
    private Byte lineStatus;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    @Excel(name = "是否已全部下发(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "if_all_issued")
    private Byte ifAllIssued;

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

    private static final long serialVersionUID = 1L;
}