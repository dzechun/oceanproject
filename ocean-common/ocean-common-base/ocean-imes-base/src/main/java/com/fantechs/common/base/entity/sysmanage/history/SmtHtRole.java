package com.fantechs.common.base.entity.sysmanage.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_ht_role")
@Data
public class SmtHtRole implements Serializable{

    private static final long serialVersionUID = -4645523137596084316L;
    /**
     * 角色历史id
     */
    @Id
    @Column(name = "ht_role_id")
    @ApiModelProperty(name="htRoleId" ,value="角色历史id")
    private String htRoleId;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    @ApiModelProperty(name="roleCode" ,value="角色编码")
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    @ApiModelProperty(name="roleName" ,value="角色名称")
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "role_desc")
    @ApiModelProperty(name="roleDesc" ,value="角色描述")
    private String roleDesc;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建用户")
    private String createUserId;

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
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改用户")
    private String modifiedUserId;

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