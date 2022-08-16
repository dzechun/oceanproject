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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 设备参数设备履历表
 * eam_ht_equipment_param
 * @author admin
 * @date 2021-06-25 17:52:35
 */
@Data
@Table(name = "eam_ht_equipment_param")
public class EamHtEquipmentParam extends ValidGroup implements Serializable {
    /**
     * 设备参数设备履历表ID
     */
    @ApiModelProperty(name="htEquipmentParamId",value = "设备参数设备履历表ID")
    @Excel(name = "设备参数设备履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_param_id")
    private Long htEquipmentParamId;

    /**
     * 设备参数设备表ID
     */
    @ApiModelProperty(name="equipmentParamId",value = "设备参数设备表ID")
    @Excel(name = "设备参数设备表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_param_id")
    private Long equipmentParamId;

    /**
     * 设备类别ID
     */
    @ApiModelProperty(name="equipmentCategoryId",value = "设备类别ID")
    @Column(name = "equipment_category_id")
    @NotNull(message = "设备类别不能为空")
    private Long equipmentCategoryId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="5",replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 设备类别名称
     */
    @ApiModelProperty(name="equipmentCategoryName",value = "设备类别名称")
    @Excel(name = "设备类别名称", height = 20, width = 30,orderNum="1")
    @Transient
    private String equipmentCategoryName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}