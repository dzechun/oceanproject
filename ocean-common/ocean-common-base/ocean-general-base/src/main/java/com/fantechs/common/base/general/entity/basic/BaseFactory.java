package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_factory")
@Data
public class BaseFactory extends ValidGroup implements Serializable {

    private static final long serialVersionUID = -2911021021255393740L;

    /**
     * id
     */
    @Id
    @Column(name = "factory_id")
    @ApiModelProperty(name="factoryId" ,value="厂别id")
    @NotNull(groups = update.class,message = "厂别id不能为空")
    private Long factoryId;

    /**
     * 工厂编码
     */
    @Column(name = "factory_code")
    @Excel(name = "工厂编码",  height = 20, width = 30, orderNum="1")
    @NotBlank(message = "厂别编码不能为空")
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    private String factoryCode;

    /**
     * 工厂名称
     */
    @Column(name = "factory_name")
    @NotBlank(message = "厂别名称不能为空")
    @Excel(name = "工厂名称", height = 20, width = 30 ,orderNum="2")
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    private String factoryName;

    /**
     * 工厂描述
     */
    @Column(name = "factory_desc")
    @Excel(name = "工厂描述", height = 20, width = 30,orderNum="3")
    @ApiModelProperty(name="factoryDesc" ,value="厂别描述")
    private String factoryDesc;

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
    @Excel(name = "备注", height = 20, width = 30 ,orderNum="9")
    private String remark;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="8",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 工厂状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status" ,value="工厂状态（0、不启用 1、启用）")
    @Excel(name = "工厂状态", height = 20, width = 30 ,orderNum="4",replace = {"不启用_0", "启用_1"})
    private Integer status;

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
