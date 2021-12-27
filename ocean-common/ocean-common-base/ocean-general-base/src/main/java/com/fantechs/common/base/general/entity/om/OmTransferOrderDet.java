package com.fantechs.common.base.general.entity.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 调拨单明细
 * om_transfer_order_det
 * @author mr.lei
 * @date 2021-06-15 10:14:57
 */
@Data
@Table(name = "om_transfer_order_det")
public class OmTransferOrderDet extends ValidGroup implements Serializable {
    /**
     * 调拨单明细ID
     */
    @ApiModelProperty(name="transferOrderDetId",value = "调拨单明细ID")
    @Id
    @Column(name = "transfer_order_det_id")
    private Long transferOrderDetId;

    /**
     * 调拨单ID
     */
    @ApiModelProperty(name="transferOrderId",value = "调拨单ID")
    @Column(name = "transfer_order_id")
    private Long transferOrderId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单据编码")
    @Column(name = "core_source_order_code")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据编码")
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 核心来源ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心来源ID")
    @Column(name = "core_source_id")
    private Long coreSourceId;

    /**
     * 来源ID
     */
    @ApiModelProperty(name="sourceId",value = "来源ID")
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="8")
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQty",value = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30,orderNum="9")
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 过期日期
     */
    @ApiModelProperty(name="expiredDate",value = "过期日期")
    @Column(name = "expired_date")
    private Date expiredDate;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 累计下发数量
     */
    @ApiModelProperty(name="totalIssueQty",value = "累计下发数量")
    @Column(name = "total_issue_qty")
    private BigDecimal totalIssueQty;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    @Column(name = "if_all_issued")
    private Byte ifAllIssued;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String remark;

    /**
     * 组织ID
     */
    @ApiModelProperty(name="orgId",value = "组织ID")
    @Column(name = "org_id")
    private Long orgId;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private static final long serialVersionUID = 1L;
}