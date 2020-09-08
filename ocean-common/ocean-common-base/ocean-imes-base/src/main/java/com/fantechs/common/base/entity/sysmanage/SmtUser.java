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
import java.util.Set;

@Table(name = "smt_user")
@Data
public class SmtUser implements Serializable {

    private static final long serialVersionUID = 8814454758985252881L;
    /**
     * 用户ID
     */
    @Id
    @Column(name = "user_id")
    @ApiModelProperty(name="userId" ,value="用户id")
    private String userId;

    /**
     * 用户帐号
     */
    @Column(name = "account")
    @ApiModelProperty(name="account" ,value="用户帐号")
    @Excel(name="用户帐号")
    private String account;

    /**
     * 用户工号
     */
    @Column(name = "user_code")
    @ApiModelProperty(name="userCode" ,value="用户编码")
    @Excel(name="用户编码")
    private String userCode;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    @ApiModelProperty(name="userName" ,value="用户名称")
    @Excel(name="用户名称")
    private String userName;

    /**
     * 密码
     */
    @Column(name = "password")
    @ApiModelProperty(name="password" ,value="密码")
    @Excel(name="密码")
    private String password;

    /**
     * 状态
     */
    @Column(name = "status")
    @ApiModelProperty(name="status" ,value="用户状态(0-无效 1-有效)")
    @Excel(name="状态",replace = {"无效_0", "有效_1"})
    //@Excel(name="状态",replace = {"0_无效", "1_有效"})
    private Integer status;

    /**
     * 厂别
     */
    @Column(name = "org_id")
    @ApiModelProperty(name="orgId" ,value="厂别")
    //@Excel(name="厂别")
    private String orgId;

    /**
     * 厂别名称
     */
    @Transient
    @Excel(name = "厂别")
    private String factoryName;

    /**
     * 部门
     */
    @Column(name = "dept_id")
    @ApiModelProperty(name="deptId" ,value="部门id")
    //@Excel(name="部门")
    private String deptId;

    /**
     * 部门名称
     */
    @Transient
    @Excel(name = "部门")
    private String deptName;

    /**
     * 电话
     */
    @Column(name = "telephone")
    @ApiModelProperty(name="telephone" ,value="电话")
    @Excel(name="分机")
    private String telephone;

    /**
     * 手机
     */
    @Column(name = "mobile")
    @ApiModelProperty(name="mobile" ,value="手机")
    @Excel(name="手机")
    private String mobile;

    /**
     * 邮件地址
     */
    @Column(name = "email")
    @ApiModelProperty(name="email" ,value="email")
    @Excel(name="邮件地址")
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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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

    /**
     *权限
     */
    @Transient
    private Set<String> authority;
}