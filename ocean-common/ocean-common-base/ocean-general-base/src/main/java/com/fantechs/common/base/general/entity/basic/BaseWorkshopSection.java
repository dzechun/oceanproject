package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "base_workshop_section")
@Data
public class BaseWorkshopSection extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 7715240650173103540L;
    /**
     * 工段ID
     */
    @Id
    @Column(name = "section_id")
    @ApiModelProperty("工段Id")
    @NotNull(groups = update.class,message = "工段id不能为空")
    private Long sectionId;

    /**
     * 工段代码
     */
    @Column(name = "section_code")
    @ApiModelProperty("工段代码")
    @Excel(name = "工段代码", height = 20, width = 30)
    @NotBlank(message = "工段代码不能为空")
    private String sectionCode;

    /**
     * 工段名称
     */
    @Column(name = "section_name")
    @ApiModelProperty("工段名称")
    @Excel(name = "工段名称", height = 20, width = 30)
    @NotBlank(message = "工段名称不能为空")
    private String sectionName;

    /**
     * 工段描述
     */
    @Column(name = "section_desc")
    @ApiModelProperty("工段描述")
    @Excel(name = "工段描述", height = 20, width = 30)
    private String sectionDesc;

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
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty("状态(0无效，1有效)")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0","有效_1"})
    private Byte status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty("创建人Id")
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
    @ApiModelProperty("创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty("修改人ID")
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
    @ApiModelProperty("修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty("逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

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
