package com.fantechs.common.base.dto.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserExcelDTO implements Serializable {

    private static final long serialVersionUID = -7071848390914738804L;
    /**
     * 用户ID
     */
    @ApiModelProperty(name="userId" ,value="用户id")
    private String userId;
    /**
     * 用户帐号
     */
    @Excel(name = "用户帐号",isImportField = "true")
    @ApiModelProperty(name="userName" ,value="用户帐号")
    private String userName;

    /**
     * 用户编码
     */
    @Excel(name = "用户编码",isImportField = "true")
    @ApiModelProperty(name="userCode" ,value="用户编码")
    private String userCode;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称",isImportField = "true")
    @ApiModelProperty(name="nickName" ,value="用户id")
    private String nickName;

    /**
     * 密码
     */
    @ApiModelProperty(name="password" ,value="用户id")
    @Excel(name="密码",isImportField = "true")
    private String password;

    /**
     * 状态
     */
    @Excel(name = "状态",replace = {"无效_0", "有效_1"},isImportField = "true")
    @ApiModelProperty(name="status" ,value="用户状态(0-关闭 1-启用)")
    private Integer status;

    /**
     * 厂别
     */
    @ApiModelProperty(name="factoryId" ,value="厂别")
    private String factoryId;

    /**
     * 厂别名称
     */
    @Transient
    @Excel(name = "厂别",isImportField = "true")
    private String factoryName;

    /**
     * 部门
     */
    @ApiModelProperty(name="deptId" ,value="部门id")
    private String deptId;

    /**
     * 部门名称
     */
    @Transient
    @Excel(name = "部门",isImportField = "true")
    private String deptName;

    /**
     * 电话
     */
    @Excel(name = "分机",isImportField = "true")
    @ApiModelProperty(name="telephone" ,value="电话")
    private String telephone;

    /**
     * 手机
     */
    @Excel(name = "手机",isImportField = "true")
    @ApiModelProperty(name="mobile" ,value="手机")
    private String mobile;

    /**
     * 邮件地址
     */
    @Excel(name = "邮件地址",isImportField = "true")
    @ApiModelProperty(name="email" ,value="email")
    private String email;

    /**
     * 创建账号
     */
    @ApiModelProperty(name="createUserId" ,value="创建用户id")
    private String createUserId;
    /**
     * 创建账号名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime" ,value="创建时间")
    private Date createTime;

    /**
     * 修改账号
     */
    @ApiModelProperty(name="modifiedUserId" ,value="修改用户id")
    private String modifiedUserId;

    /**
     * 修改账号名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    private Date modifiedTime;
}
