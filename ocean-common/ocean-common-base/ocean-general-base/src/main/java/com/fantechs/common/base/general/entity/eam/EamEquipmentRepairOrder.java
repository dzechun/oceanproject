package com.fantechs.common.base.general.entity.eam;

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

;
;

/**
 * 设备维修单表
 * eam_equipment_repair_order
 * @author admin
 * @date 2021-08-20 15:53:40
 */
@Data
@Table(name = "eam_equipment_repair_order")
public class EamEquipmentRepairOrder extends ValidGroup implements Serializable {
    /**
     * 设备维修单ID
     */
    @ApiModelProperty(name="equipmentRepairOrderId",value = "设备维修单ID")
    @Id
    @Column(name = "equipment_repair_order_id")
    @NotNull(groups = update.class,message = "设备维修单ID不能为空")
    private Long equipmentRepairOrderId;

    /**
     * 维修单号
     */
    @ApiModelProperty(name="equipmentRepairOrderCode",value = "维修单号")
    @Excel(name = "维修单号", height = 20, width = 30,orderNum="1")
    @Column(name = "equipment_repair_order_code")
    private String equipmentRepairOrderCode;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 设备条码ID
     */
    @ApiModelProperty(name="equipmentBarcodeId",value = "设备条码ID")
    @Column(name = "equipment_barcode_id")
    private Long equipmentBarcodeId;

    /**
     * 设备条码
     */
    @ApiModelProperty(name="equipmentBarcode",value = "设备条码")
    @Excel(name = "设备条码", height = 20, width = 30,orderNum="2")
    @Column(name = "equipment_barcode")
    private String equipmentBarcode;

    /**
     * 报修时间
     */
    @ApiModelProperty(name="requestForRepairTime",value = "报修时间")
    @Excel(name = "报修时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "request_for_repair_time")
    private Date requestForRepairTime;

    /**
     * 单据状态(1-待维修 2-维修中 3-已维修)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待维修 2-维修中 3-已维修)")
    @Excel(name = "单据状态(1-待维修 2-维修中 3-已维修)", height = 20, width = 30,orderNum="8")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 设备维修人员ID
     */
    @ApiModelProperty(name="repairUserId",value = "设备维修人员ID")
    @Column(name = "repair_user_id")
    private Long repairUserId;

    /**
     * 维修时间
     */
    @ApiModelProperty(name="repairTime",value = "维修时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "repair_time")
    private Date repairTime;

    /**
     * 不良责任ID
     */
    @ApiModelProperty(name="badnessDutyId",value = "不良责任ID")
    @Column(name = "badness_duty_id")
    private Long badnessDutyId;

    /**
     * 不良原因ID
     */
    @ApiModelProperty(name="badnessCauseId",value = "不良原因ID")
    @Column(name = "badness_cause_id")
    private Long badnessCauseId;

    /**
     * 报修备注
     */
    @ApiModelProperty(name="requestForRepairRemark",value = "报修备注")
    @Column(name = "request_for_repair_remark")
    private String requestForRepairRemark;

    /**
     * 维修备注
     */
    @ApiModelProperty(name="repairRemark",value = "维修备注")
    @Column(name = "repair_remark")
    private String repairRemark;

    /**
     * 不良现象ID
     */
    @ApiModelProperty(name="badnessPhenotypeId",value = "不良现象ID")
    @Column(name = "badness_phenotype_id")
    private Long badnessPhenotypeId;

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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}