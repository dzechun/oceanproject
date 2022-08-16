package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_pro_line")
@Data
public class BaseProLine extends ValidGroup implements Serializable {

    private static final long serialVersionUID = -7775679129014320519L;
    /**
     * 线别ID
     */
    @Id
    @Column(name = "pro_line_id")
    @NotNull(groups = update.class,message = "线别ID不能为空")
    private Long proLineId;

    /**
     * 线别代码
     */
    @Column(name = "pro_code")
    @ApiModelProperty(name="proCode" ,value="线别代码")
    @Excel(name = "产线编码", height = 20, width = 30)
    @NotBlank(message = "产线编码不能为空")
    private String proCode;

    /**
     * 线别名称
     */
    @Column(name = "pro_name")
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "产线名称", height = 20, width = 30)
    @NotBlank(message = "产线名称不能为空")
    private String proName;

    /**
     * 线别描述
     */
    @Column(name = "pro_desc")
    @ApiModelProperty(name="proDesc" ,value="线别描述")
    @Excel(name = "线别描述", height = 20, width = 30)
    private String proDesc;

    /**
     * 厂别ID
     */
    @Column(name = "factory_id")
    @ApiModelProperty(name="factoryId" ,value="厂别ID")
    @NotNull(message = "厂别id不能为空")
    private Long factoryId;

    /**
     * 厂别名称
     */
    @Transient
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    @Excel(name = "厂别名称", height = 20, width = 30)
    private String factoryName;

    /**
     * 厂别编码
     */
    @Transient
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    @Excel(name = "厂别编码", height = 20, width = 30)
    private String factoryCode;

    /**
     * 车间ID
     */
    @Column(name = "work_shop_id")
    @ApiModelProperty(name="workShopId" ,value="车间ID")
    private Long workShopId;

    /**
     * 车间名称
     */
    @Transient
    @ApiModelProperty(name="workShopName" ,value="车间名称")
    @Excel(name = "车间名称", height = 20, width = 30)
    private String workShopName;

    /**
     * 车间编码
     */
    @Transient
    @ApiModelProperty(name="workShopCode" ,value="车间编码")
    @Excel(name = "车间编码", height = 20, width = 30)
    private String workShopCode;

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
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 产线状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="产线状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号")
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
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号")
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
