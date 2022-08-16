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
     * 用户帐号
     */
    @Excel(name = "用户帐号(必填)", height = 20, width = 30)
    @ApiModelProperty(name="userName" ,value="用户帐号")
    private String userName;

    /**
     * 用户编码
     */
    @Excel(name = "用户编码(必填)", height = 20, width = 30)
    @ApiModelProperty(name="userCode" ,value="用户编码")
    private String userCode;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称(必填)", height = 20, width = 30)
    @ApiModelProperty(name="nickName" ,value="用户名称")
    private String nickName;

    /**
     * 密码
     */
    @ApiModelProperty(name="password" ,value="用户id")
    @Excel(name = "密码(必填)", height = 20, width = 30)
    private String password;

    /**
     * 状态
     */
    @Excel(name = "状态", height = 20, width = 30)
    @ApiModelProperty(name="status" ,value="用户状态(0-关闭 1-启用)")
    private Integer status;

    /**
     * 厂别
     */
    @ApiModelProperty(name="factoryId" ,value="厂别")
    private Long factoryId;

    /**
     * 厂别编码
     */
    @Transient
    @Excel(name = "厂别编码", height = 20, width = 30)
    private String factoryCode;

    /**
     * 部门
     */
    @ApiModelProperty(name="deptId" ,value="部门id")
    private Long deptId;

    /**
     * 部门编码
     */
    @Transient
    @Excel(name = "部门编码", height = 20, width = 30)
    private String deptCode;

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
}
