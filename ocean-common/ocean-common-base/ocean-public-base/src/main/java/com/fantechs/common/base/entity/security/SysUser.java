package com.fantechs.common.base.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long userId;

    /**
     * 用户帐号
     */
    @Column(name = "user_name")
    @ApiModelProperty(name="userName" ,value="用户帐号")
    @Excel(name="用户帐号", height = 20, width = 30)
    @NotBlank(message = "用户帐号不能为空")
    private String userName;

    /**
     * 用户工号
     */
    @Column(name = "user_code")
    @ApiModelProperty(name="userCode" ,value="用户编码")
    @Excel(name="用户编码", height = 20, width = 30)
    @NotBlank(message = "用户编码不能为空")
    private String userCode;

    /**
     * 用户名称
     */
    @Column(name = "nick_name")
    @ApiModelProperty(name="nickName" ,value="用户名称")
    @Excel(name="用户名称", height = 20, width = 30)
    private String nickName;

    /**
     * 密码
     */
    @ApiModelProperty(name="password" ,value="密码")
    @NotBlank(groups = add.class,message = "用户密码不能为空")
    private String password;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="用户状态(0-无效 1-有效)")
    @Excel(name="状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Byte status;

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
    @Excel(name = "厂别名称",height = 20, width = 30)
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
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
    @Excel(name = "部门",height = 20, width = 30)
    @ApiModelProperty(name="deptName" ,value="部门名称")
    private String deptName;


    /**
     * 电话
     */
    @ApiModelProperty(name="telephone" ,value="电话")
    @Excel(name="分机",height = 20, width = 30)
    private String telephone;

    /**
     * 手机
     */
    @ApiModelProperty(name="mobile" ,value="手机")
    @Excel(name="手机",height = 20, width = 30)
    private String mobile;

    /**
     * 邮件地址
     */

    @ApiModelProperty(name="email" ,value="email")
    @Excel(name="邮件地址",height = 20, width = 30)
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
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
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

    /**
     *用户所属组织列表
     */
    @Transient
    private List<String> organizationIds;

    /**
     * 菜单字符串
     */
    @Transient
    private String menu;

    /**
     * 角色id
     */
    @ApiModelProperty(name="roleId" ,value="角色id")
    @Transient
    private Long roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty(name="roleName" ,value="角色名称")
    @Transient
    private Long roleName;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName" ,value="供应商名称")
    @Transient
    private String supplierName;

    /**
     * 供应商编码
     */
    @ApiModelProperty(name="supplierCode" ,value="供应商编码")
    @Transient
    private String supplierCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId" ,value="供应商ID")
    @Transient
    private Long supplierId;

}
