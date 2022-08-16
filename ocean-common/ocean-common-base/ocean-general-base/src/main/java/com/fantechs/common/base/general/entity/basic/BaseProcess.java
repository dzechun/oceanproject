package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "base_process")
@Data
public class BaseProcess extends ValidGroup implements Serializable {
    private static final long serialVersionUID = -7245414479951156391L;
    /**
     * 工序ID
     */
    @Id
    @Column(name = "process_id")
    @ApiModelProperty(name="processId" ,value="工序ID")
    @NotNull(groups = update.class,message = "工序ID不能为空")
    private Long processId;

    /**
     * 工序代码
     */
    @Column(name = "process_code")
    @ApiModelProperty(name="processCode" ,value="工序代码")
    @Excel(name = "工序代码", height = 20, width = 30)
    @NotBlank(message = "工序编码不能为空")
    private String processCode;

    /**
     * 工序名称
     */
    @Column(name = "process_name")
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30)
    @NotBlank(message = "工序名称不能为空")
    private String processName;

    /**
     * 工序描述
     */
    @Column(name = "process_desc")
    @ApiModelProperty(name="processDesc" ,value="工序描述")
    @Excel(name = "工序描述", height = 20, width = 30)
    private String processDesc;

    /**
     * 工段ID
     */
    @Column(name = "section_id")
    @ApiModelProperty(name="sectionId" ,value="工段ID")
    private Long sectionId;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name="sectionName" ,value="工段名称")
    @Excel(name = "所属工段", height = 20, width = 30)
    private String sectionName;

    /**
     * 工段编码
     */
    @Transient
    @ApiModelProperty(name="sectionCode" ,value="工段编码")
    @Excel(name = "工段编码", height = 20, width = 30)
    private String sectionCode;

    /**
     * 工序类别ID
     */
    @Column(name = "process_category_id")
    @ApiModelProperty(name="processCategoryId" ,value="工序类别ID")
    @NotNull(message = "工序类别id不能为空")
    private Long processCategoryId;

    /**
     * 工序类别名称
     */
    @Transient
    @ApiModelProperty(name="processCategoryName" ,value="工序类别名称")
    @Excel(name = "工序类别",  height = 20, width = 30)
    private String processCategoryName;

    /**
     * 工序类别编码
     */
    @Transient
    @ApiModelProperty(name="processCategoryCode" ,value="工序类别编码")
    @Excel(name = "工序类别编码",  height = 20, width = 30)
    private String processCategoryCode;

    /**
     * 是否报工扫描（0、否 1、是）
     */
    @Column(name = "is_job_scan")
    @ApiModelProperty(name="isJobScan" ,value="是否报工扫描")
    @Excel(name = "是否报工扫描",  height = 20, width = 30,replace = {"否_0", "是_1"})
    private Byte isJobScan;

    /**
     * 是否开工扫描（0、否 1、是）
     */
    @Column(name = "is_start_scan")
    @ApiModelProperty(name= "isStartScan" ,value="是否开工扫描")
    @Excel(name = "是否开工扫描",  height = 20, width = 30,replace = {"否_0", "是_1"})
    private Byte isStartScan;

    /**
     * 是否品质确认（0、否 1、是）
     */
    @Column(name = "is_quality")
    @ApiModelProperty(name="isQuality" ,value="是否品质确认")
    @Excel(name = "是否品质确认",  height = 20, width = 30,replace = {"否_0", "是_1"})
    private Byte isQuality;

    /**
     * 完成时间
     */
    @Column(name = "finish_time")
    @ApiModelProperty(name="finishTime" ,value="完成时间")
    private BigDecimal finishTime;


    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除")
    private Byte isDelete;

    /**
     * 有效开始时间
     */
    @Transient
    @ApiModelProperty(name="effectiveStartTime" ,value="有效开始时间")
    private Date effectiveStartTime;

    /**
     * 有效结束时间
     */
    @Transient
    @ApiModelProperty(name="effectiveEndTime" ,value="有效结束时间")
    private Date effectiveEndTime;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;

}
