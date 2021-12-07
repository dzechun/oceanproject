package com.fantechs.common.base.general.entity.qms;

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
 * 来料检验单
 * qms_incoming_inspection_order
 * @author admin
 * @date 2021-12-06 10:30:33
 */
@Data
@Table(name = "qms_incoming_inspection_order")
public class QmsIncomingInspectionOrder extends ValidGroup implements Serializable {
    /**
     * 来料检验单ID
     */
    @ApiModelProperty(name="incomingInspectionOrderId",value = "来料检验单ID")
    @Id
    @Column(name = "incoming_inspection_order_id")
    private Long incomingInspectionOrderId;

    /**
     * 来源单据类型(1-创建 2-ASN单 3-第三方系统 4-采购订单 5-收货计划单 6-收货作业单)
     */
    @ApiModelProperty(name="sourceOrderType",value = "来源单据类型(1-创建 2-ASN单 3-第三方系统 4-采购订单 5-收货计划单 6-收货作业单)")
    @Excel(name = "来源单据类型(1-创建 2-ASN单 3-第三方系统 4-采购订单 5-收货计划单 6-收货作业单)", height = 20, width = 30,orderNum="3",needMerge = true)
    @Column(name = "source_order_type")
    private Byte sourceOrderType;

    /**
     * 来源单号
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单号")
    @Excel(name = "来源单号", height = 20, width = 30,orderNum="2",needMerge = true)
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 来料检验单号
     */
    @ApiModelProperty(name="incomingInspectionOrderCode",value = "来料检验单号")
    @Excel(name = "来料检验单号", height = 20, width = 30,orderNum="1",needMerge = true)
    @Column(name = "incoming_inspection_order_code")
    private String incomingInspectionOrderCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 单据数量
     */
    @ApiModelProperty(name="orderQty",value = "单据数量")
    @Excel(name = "单据数量", height = 20, width = 30,orderNum="10",needMerge = true)
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId",value = "检验标准ID")
    @Column(name = "inspection_standard_id")
    private Long inspectionStandardId;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    @Column(name = "inspection_way_id")
    private Long inspectionWayId;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    @Excel(name = "检验状态(1-待检验 2-检验中 3-已检验)", height = 20, width = 30,orderNum="13",needMerge = true)
    @Column(name = "inspection_status")
    private Byte inspectionStatus;

    /**
     * 检验结果(0-不合格 1-合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-不合格 1-合格)")
    @Excel(name = "检验结果(0-不合格 1-合格)", height = 20, width = 30,orderNum="14",needMerge = true)
    @Column(name = "inspection_result")
    private Byte inspectionResult;

    /**
     * MRB评审(1-特采 2-挑选使用 3-退供应商)
     */
    @ApiModelProperty(name="mrbResult",value = "MRB评审(1-特采 2-挑选使用 3-退供应商)")
    @Excel(name = "MRB评审(1-特采 2-挑选使用 3-退供应商)", height = 20, width = 30,orderNum="15",needMerge = true)
    @Column(name = "mrb_result")
    private Byte mrbResult;

    /**
     * 文件表ID
     */
    @ApiModelProperty(name="fileId",value = "文件表ID")
    @Column(name = "file_id")
    private Long fileId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="18",exportFormat ="yyyy-MM-dd HH:mm:ss",needMerge = true)
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="20",exportFormat ="yyyy-MM-dd HH:mm:ss",needMerge = true)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;


    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}