package com.fantechs.common.base.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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
}