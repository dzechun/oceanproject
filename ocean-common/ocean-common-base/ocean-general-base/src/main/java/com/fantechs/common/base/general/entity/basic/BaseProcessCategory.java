package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "base_process_category")
@Data
public class BaseProcessCategory extends ValidGroup implements Serializable {
    /**
     * 工序类别ID
     */
    @Id
    @Column(name = "process_category_id")
    @NotNull(groups = update.class,message = "工序类别id不能为空")
    @ApiModelProperty(name="processCategoryId" ,value="工序类别ID")
    private Long processCategoryId;

    /**
     * 工序类别代码
     */
    @Column(name = "process_category_code")
    @NotBlank(message = "工序类别代码不能为空")
    @Excel(name = "工序类别编码",  height = 20, width = 30, orderNum="1")
    @ApiModelProperty(name="processCategoryCode" ,value="工序类别代码")
    private String processCategoryCode;

    /**
     * 工序类别名称
     */
    @Column(name = "process_category_name")
    @NotBlank(message = "工序类别名称不能为空")
    @Excel(name = "工序类别名称",  height = 20, width = 30, orderNum="2")
    @ApiModelProperty(name="processCategoryName" ,value="工序类别名称")
    private String processCategoryName;

    /**
     * 工序类别描述
     */
    @Column(name = "process_category_desc")
    @Excel(name = "工序类别描述", height = 20, width = 30,orderNum="3")
    @ApiModelProperty(name="processCategoryDesc" ,value="工序类别描述")
    private String processCategoryDesc;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态（0、无效 1、有效）")
    @Excel(name = "工厂类别状态", height = 20, width = 30 ,orderNum="4",replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
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
