package com.fantechs.common.base.entity.sysmanage;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_role")
@Data
public class SmtRole implements Serializable {
    private static final long serialVersionUID = -6390912935642574125L;
    /**
     * 角色id
     */
    @Id
    @Column(name = "role_id")
    @ApiModelProperty(name="roleId" ,value="角色id")
    private Long roleId;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    @ApiModelProperty(name="roleCode" ,value="角色编码")
    @Excel(name = "角色编码", height = 20, width = 30)
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    @ApiModelProperty(name="roleName" ,value="角色名称")
    @Excel(name = "角色名称", height = 20, width = 30)
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "role_desc")
    @ApiModelProperty(name="roleDesc" ,value="角色描述")
    @Excel(name = "角色描述", height = 20, width = 30)
    private String roleDesc;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建用户")
    @Excel(name = "创建用户", height = 20, width = 30)
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Transient
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改用户")
    @Excel(name = "修改用户", height = 20, width = 30)
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Transient
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 角色状态（0、不启用 1、启用）
     */
    @Column(name = "status")
    @ApiModelProperty(name="status" ,value="角色状态（0、不启用 1、启用）")
    private Integer status;
    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1" ,value="扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2" ,value="扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3" ,value="扩展字段3")
    private String option3;

}