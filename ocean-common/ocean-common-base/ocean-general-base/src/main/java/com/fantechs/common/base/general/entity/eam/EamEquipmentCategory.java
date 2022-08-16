package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 设备类别
 * eam_equipment_category
 * @author admin
 * @date 2021-06-25 11:14:57
 */
@Data
@Table(name = "eam_equipment_category")
public class EamEquipmentCategory extends ValidGroup implements Serializable {
    /**
     * 设备类别ID
     */
    @ApiModelProperty(name="equipmentCategoryId",value = "设备类别ID")
    @Id
    @Column(name = "equipment_category_id")
    @NotNull(groups = update.class,message = "设备类别ID不能为空")
    private Long equipmentCategoryId;

    /**
     * 周转工具编码
     */
    @ApiModelProperty(name="equipmentCategoryCode",value = "周转工具编码")
    @Excel(name = "周转工具编码", height = 20, width = 30,orderNum="1")
    @Column(name = "equipment_category_code")
    @NotBlank(message = "设备类别编码不能为空")
    private String equipmentCategoryCode;

    /**
     * 设备类别名称
     */
    @ApiModelProperty(name="equipmentCategoryName",value = "设备类别名称")
    @Excel(name = "设备类别名称", height = 20, width = 30,orderNum="1")
    @Column(name = "equipment_category_name")
    @NotBlank(message = "设备类别名称不能为空")
    private String equipmentCategoryName;

    /**
     * 设备类别描述
     */
    @ApiModelProperty(name="equipmentCategoryDesc",value = "设备类别描述")
    @Excel(name = "设备类别描述", height = 20, width = 30,orderNum="2")
    @Column(name = "equipment_category_desc")
    private String equipmentCategoryDesc;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="3",replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="5",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}