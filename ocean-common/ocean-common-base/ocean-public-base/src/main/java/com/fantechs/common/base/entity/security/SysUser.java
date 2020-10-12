package com.fantechs.common.base.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "sys_user")
@Data
public class SysUser extends ValidGroup implements Serializable {
    private static final long serialVersionUID = -3448406995774680062L;
    /**
     * 用户ID
     */
    @Id
    @Column(name = "user_id")
    @ApiModelProperty(name="userId" ,value="用户id")
    @NotNull(groups= update.class,message = "用户Id不能为空")
    private Long userId;

    /**
     * 用户帐号
     */
    @Column(name = "user_name")
    @ApiModelProperty(name="userName" ,value="用户帐号")
    @Excel(name="用户帐号")
    @NotBlank(message = "用户帐号不能为空")
    private String userName;

    /**
     * 用户工号
     */
    @Column(name = "user_code")
    @ApiModelProperty(name="userCode" ,value="用户编码")
    @Excel(name="用户编码")
    @NotBlank(message = "用户编码不能为空")
    private String userCode;

    /**
     * 用户名称
     */
    @Column(name = "nick_name")
    @ApiModelProperty(name="nickName" ,value="用户名称")
    @Excel(name="用户名称")
    private String nickName;

    /**
     * 密码
     */
    @ApiModelProperty(name="password" ,value="密码")
    @Excel(name="密码")
    @NotBlank(message = "用户密码不能为空")
    private String password;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="用户状态(0-无效 1-有效)")
    @Excel(name="状态",replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 厂别id
     */
    @Column(name = "factory_id")
    @ApiModelProperty(name="factoryId" ,value="厂别id")
    private Long factoryId;

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
    private Long deptId;

    /**
     * 部门名称
     */
    @Transient
    @Excel(name = "部门")
    private String deptName;


    /**
     * 电话
     */
    @ApiModelProperty(name="telephone" ,value="电话")
    @Excel(name="分机")
    private String telephone;

    /**
     * 手机
     */
    @ApiModelProperty(name="mobile" ,value="手机")
    @Excel(name="手机")
    private String mobile;

    /**
     * 邮件地址
     */

    @ApiModelProperty(name="email" ,value="email")
    @Excel(name="邮件地址")
    private String email;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建人
     */
    @ApiModelProperty(name="createUserName" ,value="创建人")
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
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改人
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人")
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
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
     *权限
     */
    @Transient
    @JSONField(serialize = false)
    private Set<String> authority;
}