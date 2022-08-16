package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 治具领用
 * eam_jig_requisition
 * @author admin
 * @date 2021-07-30 09:22:00
 */
@Data
@Table(name = "eam_jig_requisition")
public class EamJigRequisition extends ValidGroup implements Serializable {
    /**
     * 治具领用ID
     */
    @ApiModelProperty(name="jigRequisitionId",value = "治具领用ID")
    @Id
    @Column(name = "jig_requisition_id")
    private Long jigRequisitionId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Column(name = "jig_barcode_id")
    private Long jigBarcodeId;

    /**
     * 使用用户ID
     */
    @ApiModelProperty(name="usageUserId",value = "使用用户ID")
    @Column(name = "usage_user_id")
    private Long usageUserId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="18",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="20",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 本次使用次数
     */
    @Transient
    @ApiModelProperty(name = "thisTimeUsageTime",value = "本次使用次数")
    @Excel(name = "本次使用次数", height = 20, width = 30,orderNum="15")
    private Integer thisTimeUsageTime;

    /**
     * 新工单ID
     */
    @ApiModelProperty(name="newWorkOrderId",value = "新工单ID")
    @Transient
    private Long newWorkOrderId;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}