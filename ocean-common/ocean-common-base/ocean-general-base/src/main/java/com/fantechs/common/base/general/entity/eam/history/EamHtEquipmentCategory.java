package com.fantechs.common.base.general.entity.eam.history;

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
 * 设备类别履历表
 * eam_ht_equipment_category
 * @author admin
 * @date 2021-06-25 11:14:58
 */
@Data
@Table(name = "eam_ht_equipment_category")
public class EamHtEquipmentCategory extends ValidGroup implements Serializable {
    /**
     * 设备类别履历ID
     */
    @ApiModelProperty(name="htEquipmentCategoryId",value = "设备类别履历ID")
    @Excel(name = "设备类别履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_category_id")
    private Long htEquipmentCategoryId;

    /**
     * 设备类别ID
     */
    @ApiModelProperty(name="equipmentCategoryId",value = "设备类别ID")
    @Excel(name = "设备类别ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_category_id")
    private Long equipmentCategoryId;

    /**
     * 周转工具编码
     */
    @ApiModelProperty(name="equipmentCategoryCode",value = "周转工具编码")
    @Excel(name = "周转工具编码", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_category_code")
    private String equipmentCategoryCode;

    /**
     * 设备类别描述
     */
    @ApiModelProperty(name="equipmentCategoryDesc",value = "设备类别描述")
    @Excel(name = "设备类别描述", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_category_desc")
    private String equipmentCategoryDesc;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}