package com.fantechs.common.base.general.entity.eam.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 设备保养单履历表
 * eam_ht_equipment_maintain_order
 * @author bgkun
 * @date 2021-08-21 11:47:07
 */
@Data
@Table(name = "eam_ht_equipment_maintain_order")
public class EamHtEquipmentMaintainOrder extends ValidGroup implements Serializable {
    /**
     * 设备保养单履历ID
     */
    @ApiModelProperty(name="htEquipmentMaintainOrderId",value = "设备保养单履历ID")
    @Excel(name = "设备保养单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_maintain_order_id")
    private Long htEquipmentMaintainOrderId;

    /**
     * 设备保养单ID
     */
    @ApiModelProperty(name="equipmentMaintainOrderId",value = "设备保养单ID")
    @Excel(name = "设备保养单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_maintain_order_id")
    private Long equipmentMaintainOrderId;

    /**
     * 设备保养单号
     */
    @ApiModelProperty(name="equipmentMaintainOrderCode",value = "设备保养单号")
    @Excel(name = "设备保养单号", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_maintain_order_code")
    private String equipmentMaintainOrderCode;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Excel(name = "设备ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 设备条码ID
     */
    @ApiModelProperty(name="equipmentBarcodeId",value = "设备条码ID")
    @Excel(name = "设备条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_barcode_id")
    private Long equipmentBarcodeId;

    /**
     * 设备保养项目ID
     */
    @ApiModelProperty(name="equipmentMaintainProjectId",value = "设备保养项目ID")
    @Excel(name = "设备保养项目ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_maintain_project_id")
    private Long equipmentMaintainProjectId;

    /**
     * 单据状态(1-待保养 2-已保养)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待保养 2-已保养)")
    @Excel(name = "单据状态(1-待保养 2-已保养)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

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
     * 设备编码
     */
    @Transient
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="3")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @Transient
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="4")
    private String equipmentName;

    /**
     * 设备类别名称
     */
    @Transient
    @ApiModelProperty(name="equipmentCategoryName",value = "设备类别名称")
    @Excel(name = "设备类别名称", height = 20, width = 30,orderNum="5")
    private String equipmentCategoryName;

    /**
     * 设备条码
     */
    @Transient
    @ApiModelProperty(name="equipmentBarcode",value = "设备条码")
    @Excel(name = "设备条码", height = 20, width = 30,orderNum="2")
    private String equipmentBarcode;

    /**
     * 设备保养项目编码
     */
    @Transient
    @ApiModelProperty(name="equipmentMaintainProjectCode",value = "设备保养项目编码")
    @Excel(name = "设备保养项目编码", height = 20, width = 30,orderNum="6")
    private String equipmentMaintainProjectCode;

    /**
     * 设备保养项目名称
     */
    @Transient
    @ApiModelProperty(name="equipmentMaintainProjectName",value = "设备保养项目名称")
    @Excel(name = "设备保养项目名称", height = 20, width = 30,orderNum="7")
    private String equipmentMaintainProjectName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}