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
 * 设备点检项目履历表
 * eam_ht_equ_point_inspection_project
 * @author bgkun
 * @date 2021-08-20 14:19:04
 */
@Data
@Table(name = "eam_ht_equ_point_inspection_project")
public class EamHtEquPointInspectionProject extends ValidGroup implements Serializable {
    /**
     * 设备点检项目履历ID
     */
    @ApiModelProperty(name="htEquPointInspectionProjectId",value = "设备点检项目履历ID")
    @Excel(name = "设备点检项目履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equ_point_inspection_project_id")
    private Long htEquPointInspectionProjectId;

    /**
     * 设备点检项目ID
     */
    @ApiModelProperty(name="equPointInspectionProjectId",value = "设备点检项目ID")
    @Excel(name = "设备点检项目ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equ_point_inspection_project_id")
    private Long equPointInspectionProjectId;

    /**
     * 设备点检编码
     */
    @ApiModelProperty(name="equPointInspectionProjectCode",value = "设备点检编码")
    @Excel(name = "设备点检编码", height = 20, width = 30,orderNum="") 
    @Column(name = "equ_point_inspection_project_code")
    private String equPointInspectionProjectCode;

    /**
     * 设备点检名称
     */
    @ApiModelProperty(name="equPointInspectionProjectName",value = "设备点检名称")
    @Excel(name = "设备点检名称", height = 20, width = 30,orderNum="") 
    @Column(name = "equ_point_inspection_project_name")
    private String equPointInspectionProjectName;

    /**
     * 设备点检内容
     */
    @ApiModelProperty(name="equPointInspectionProjectDesc",value = "设备点检内容")
    @Excel(name = "设备点检内容", height = 20, width = 30,orderNum="") 
    @Column(name = "equ_point_inspection_project_desc")
    private String equPointInspectionProjectDesc;

    /**
     * 设备类别ID
     */
    @ApiModelProperty(name="equipmentCategoryId",value = "设备类别ID")
    @Excel(name = "设备类别ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_category_id")
    private Long equipmentCategoryId;

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