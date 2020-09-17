package com.fantechs.common.base.entity.sysmanage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_user_role")
@Data
public class SmtUserRole implements Serializable {

    private static final long serialVersionUID = -7109497559493175283L;
    /**
     * id
     */
    @Id
    @Column(name = "user_role_id")
    private Long userRoleId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    @ApiModelProperty(name="roleId" ,value="角色id")
    private Long roleId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    @ApiModelProperty(name="userId" ,value="用户id")
    private Long userId;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号")
    private Long createUserId;

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
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    private Date modifiedTime;

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