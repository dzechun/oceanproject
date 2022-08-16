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
import java.util.List;

;
;

/**
 * 设备保养项目
 * eam_equipment_maintain_project
 * @author bgkun
 * @date 2021-08-21 11:47:06
 */
@Data
@Table(name = "eam_equipment_maintain_project")
public class EamEquipmentMaintainProject extends ValidGroup implements Serializable {
    /**
     * 设备保养项目ID
     */
    @ApiModelProperty(name="equipmentMaintainProjectId",value = "设备保养项目ID")
    @Id
    @Column(name = "equipment_maintain_project_id")
    private Long equipmentMaintainProjectId;

    /**
     * 设备保养编码
     */
    @ApiModelProperty(name="equipmentMaintainProjectCode",value = "设备保养编码")
    @Excel(name = "设备保养编码", height = 20, width = 30,orderNum="1")
    @Column(name = "equipment_maintain_project_code")
    private String equipmentMaintainProjectCode;

    /**
     * 设备保养名称
     */
    @ApiModelProperty(name="equipmentMaintainProjectName",value = "设备保养名称")
    @Excel(name = "设备保养名称", height = 20, width = 30,orderNum="2")
    @Column(name = "equipment_maintain_project_name")
    private String equipmentMaintainProjectName;

    /**
     * 设备保养内容
     */
    @ApiModelProperty(name="equipmentMaintainProjectDesc",value = "设备保养内容")
    @Excel(name = "设备保养内容", height = 20, width = 30,orderNum="3")
    @Column(name = "equipment_maintain_project_desc")
    private String equipmentMaintainProjectDesc;

    /**
     * 设备类别ID
     */
    @ApiModelProperty(name="equipmentCategoryId",value = "设备类别ID")
    @Column(name = "equipment_category_id")
    private Long equipmentCategoryId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="6")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="5")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;

    /**
     * 保养项目事项集合
     */
    @Transient
    @ApiModelProperty(name = "items",value = "保养项目事项集合")
    private List<EamEquipmentMaintainProjectItem> items;
}