package com.fantechs.common.base.dto.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class SysUserExcelDTO {

    private static final long serialVersionUID = -7071848390914738804L;
    /**
     * 用户ID
     */
    @ApiModelProperty(name="userId" ,value="用户id")
    private String userId;
    /**
     * 用户帐号
     */
    @Excel(name = "用户帐号", height = 20, width = 30)
    @ApiModelProperty(name="userName" ,value="用户帐号")
    private String userName;

    /**
     * 用户工号
     */
    @Excel(name = "用户工号", height = 20, width = 30)
    @ApiModelProperty(name="userCode" ,value="用户编码")
    private String userCode;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称", height = 20, width = 30)
    @ApiModelProperty(name="nickName" ,value="用户id")
    private String nickName;

    /**
     * 密码
     */
    @ApiModelProperty(name="password" ,value="用户id")
    private String password;

    /**
     * 状态
     */
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    @ApiModelProperty(name="status" ,value="用户状态(0-关闭 1-启用)")
    private Integer status;

    /**
     * 厂别
     */
    //@Excel(name = "厂别", height = 20, width = 30)
    @ApiModelProperty(name="factoryId" ,value="厂别")
    private String factoryId;

    /**
     * 厂别名称
     */
    @Transient
    @Excel(name = "厂别名称", height = 20, width = 30)
    private String factoryName;

    /**
     * 部门
     */
    //@Excel(name = "部门", height = 20, width = 30)
    @ApiModelProperty(name="deptId" ,value="部门id")
    private String deptId;

    /**
     * 部门名称
     */
    @Transient
    @Excel(name = "部门名称", height = 20, width = 30)
    private String deptName;

    /**
     * 电话
     */
    @Excel(name = "电话", height = 20, width = 30)
    @ApiModelProperty(name="telephone" ,value="电话")
    private String telephone;

    /**
     * 手机
     */
    @Excel(name = "手机", height = 20, width = 30)
    @ApiModelProperty(name="mobile" ,value="手机")
    private String mobile;

    /**
     * 邮件地址
     */
    @Excel(name = "邮件地址", height = 20, width = 30)
    @ApiModelProperty(name="email" ,value="email")
    private String email;

    /**
     * 创建账号
     */
    //@Excel(name = "创建账号", height = 20, width = 30)
    @ApiModelProperty(name="createUserId" ,value="创建用户id")
    private String createUserId;
    /**
     * 创建账号名称
     */
    @Excel(name = "创建账号", height = 20, width = 30)
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    private Date createTime;

    /**
     * 修改账号
     */
    //@Excel(name = "修改账号", height = 20, width = 30)
    @ApiModelProperty(name="modifiedUserId" ,value="修改用户id")
    private String modifiedUserId;

    /**
     * 修改账号名称
     */
    @Excel(name = "修改账号", height = 20, width = 30)
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    private Date modifiedTime;
}
