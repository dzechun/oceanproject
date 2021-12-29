package com.fantechs.common.base.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_role")
@Data
public class SysRole extends ValidGroup implements Serializable {
    private static final long serialVersionUID = -6986029074414768572L;
    /**
     * id
     */
    @Id
    @Column(name = "role_id")
    @ApiModelProperty(name="roleId" ,value="角色id")
    @NotNull(groups = update.class,message ="角色Id不能为空" )
    private Long roleId;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    @ApiModelProperty(name="roleCode" ,value="角色编码")
    @NotBlank(message = "角色编码不能为空")
    @Excel(name = "角色编码", height = 20, width = 30,orderNum = "1")
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    @ApiModelProperty(name="roleName" ,value="角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Excel(name = "角色名称", height = 20, width = 30,orderNum = "2")
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "role_desc")
    @ApiModelProperty(name="roleDesc" ,value="角色描述")
    @Excel(name = "角色描述", height = 20, width = 30,orderNum = "3")
    private String roleDesc;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人id")
    private Long createUserId;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum = "5",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人id
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人id")
    private Long modifiedUserId;


    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum = "7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 角色状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status" ,value="角色状态（0、不启用 1、启用）")
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum = "4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum = "6")
    private String modifiedUserName;

    /**
     * 菜单ID
     */
    @Transient
    private Long menuId;
}
