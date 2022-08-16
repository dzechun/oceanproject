package com.fantechs.common.base.general.entity.eam.history;

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
 * 设备保养单明细履历表
 * eam_ht_equipment_maintain_order_det
 * @author bgkun
 * @date 2021-08-21 11:47:07
 */
@Data
@Table(name = "eam_ht_equipment_maintain_order_det")
public class EamHtEquipmentMaintainOrderDet extends ValidGroup implements Serializable {
    /**
     * 设备保养单明细履历ID
     */
    @ApiModelProperty(name="htEquipmentMaintainOrderDetId",value = "设备保养单明细履历ID")
    @Excel(name = "设备保养单明细履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_maintain_order_det_id")
    private Long htEquipmentMaintainOrderDetId;

    /**
     * 设备保养单明细ID
     */
    @ApiModelProperty(name="equipmentMaintainOrderDetId",value = "设备保养单明细ID")
    @Excel(name = "设备保养单明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_maintain_order_det_id")
    private Long equipmentMaintainOrderDetId;

    /**
     * 设备保养单ID
     */
    @ApiModelProperty(name="equipmentMaintainOrderId",value = "设备保养单ID")
    @Excel(name = "设备保养单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_maintain_order_id")
    private Long equipmentMaintainOrderId;

    /**
     * 设备保养事项ID
     */
    @ApiModelProperty(name="equipmentMaintainProjectItemId",value = "设备保养事项ID")
    @Excel(name = "设备保养事项ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_maintain_project_item_id")
    private Long equipmentMaintainProjectItemId;

    /**
     * 定量结果
     */
    @ApiModelProperty(name="rationResult",value = "定量结果")
    @Excel(name = "定量结果", height = 20, width = 30,orderNum="") 
    @Column(name = "ration_result")
    private BigDecimal rationResult;

    /**
     * 定性结果(0-否 1-是)
     */
    @ApiModelProperty(name="qualitative",value = "定性结果(0-否 1-是)")
    @Excel(name = "定性结果(0-否 1-是)", height = 20, width = 30,orderNum="") 
    private Byte qualitative;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}