package com.fantechs.common.base.entity.sysmanage.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_ht_user")
@Data
public class SmtHtUser implements Serializable {
    private static final long serialVersionUID = 5247919088431420300L;
    /**
     * 用户历史id
     */
    @Id
    @Column(name = "ht_user_id")
    @ApiModelProperty(name="userId" ,value="用户历史id")
    private String htUserId;

    /**
     * 用户帐号
     */
    @Column(name = "account")
    @ApiModelProperty(name="account" ,value="用户帐号")
    private String account;

    /**
     * 用户工号
     */
    @Column(name = "user_code")
    @ApiModelProperty(name="userCode" ,value="用户编码")
    private String userCode;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    @ApiModelProperty(name="userName" ,value="用户名称")
    private String userName;

    /**
     * 密码
     */
    @Column(name = "password")
    @ApiModelProperty(name="password" ,value="密码")
    private String password;

    /**
     * 状态
     */
    @Column(name = "status")
    @ApiModelProperty(name="status" ,value="用户状态(0-关闭 1-启用)")
    private Integer status;

    /**
     * 厂别
     */
    @Column(name = "org_id")
    @ApiModelProperty(name="orgId" ,value="厂别")
    private String orgId;

    /**
     * 部门
     */
    @Column(name = "dept_id")
    @ApiModelProperty(name="deptId" ,value="部门id")
    private String deptId;

    /**
     * 电话
     */
    @Column(name = "telephone")
    @ApiModelProperty(name="telephone" ,value="电话")
    private String telephone;

    /**
     * 手机
     */
    @Column(name = "mobile")
    @ApiModelProperty(name="mobile" ,value="手机")
    private String mobile;

    /**
     * 邮件地址
     */
    @Column(name = "email")
    @ApiModelProperty(name="email" ,value="email")
    private String email;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建用户id")
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
    @ApiModelProperty(name="modifiedUserId" ,value="修改用户id")
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