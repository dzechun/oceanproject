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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 治具点检项目
 * eam_jig_point_inspection_project
 * @author admin
 * @date 2021-08-16 09:33:29
 */
@Data
@Table(name = "eam_jig_point_inspection_project")
public class EamJigPointInspectionProject extends ValidGroup implements Serializable {
    /**
     * 治具点检项目ID
     */
    @ApiModelProperty(name="jigPointInspectionProjectId",value = "治具点检项目ID")
    @Id
    @Column(name = "jig_point_inspection_project_id")
    @NotNull(groups = update.class,message = "治具点检项目ID不能为空")
    private Long jigPointInspectionProjectId;

    /**
     * 治具点检编码
     */
    @ApiModelProperty(name="jigPointInspectionProjectCode",value = "治具点检编码")
    @Excel(name = "治具点检编码", height = 20, width = 30,orderNum="1")
    @Column(name = "jig_point_inspection_project_code")
    @NotBlank(message = "治具点检编码不能为空")
    private String jigPointInspectionProjectCode;

    /**
     * 治具点检名称
     */
    @ApiModelProperty(name="jigPointInspectionProjectName",value = "治具点检名称")
    @Excel(name = "治具点检名称", height = 20, width = 30,orderNum="2")
    @Column(name = "jig_point_inspection_project_name")
    @NotBlank(message = "治具点检名称不能为空")
    private String jigPointInspectionProjectName;

    /**
     * 治具点检内容
     */
    @ApiModelProperty(name="jigPointInspectionProjectDesc",value = "治具点检内容")
    @Excel(name = "治具点检内容", height = 20, width = 30,orderNum="3")
    @Column(name = "jig_point_inspection_project_desc")
    private String jigPointInspectionProjectDesc;

    /**
     * 治具类别ID
     */
    @ApiModelProperty(name="jigCategoryId",value = "治具类别ID")
    @Column(name = "jig_category_id")
    @NotNull(message = "治具类别不能为空")
    private Long jigCategoryId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="6",replace = {"无效_0","有效_1"})
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

    /**
     * 治具点检事项列表
     */
    @ApiModelProperty(name="list",value = "治具点检事项列表")
    private List<EamJigPointInspectionProjectItem> list = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}