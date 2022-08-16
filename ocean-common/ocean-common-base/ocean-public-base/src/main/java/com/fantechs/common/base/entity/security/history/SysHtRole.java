package com.fantechs.common.base.entity.security.history;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_ht_role")
@Data
public class SysHtRole implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "ht_role_id")
    @ApiModelProperty(name="htRoleId" ,value="id")
    private Long htRoleId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    @ApiModelProperty(name="roleId" ,value="角色id")
    private Long roleId;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    @ApiModelProperty(name="roleCode" ,value="角色编码")
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    @ApiModelProperty(name="roleName" ,value="角色名称")
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "role_desc")
    @ApiModelProperty(name="roleDesc" ,value="角色描述")
    private String roleDesc;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人id")
    private Long createUserId;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
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
     * 修改人id
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人id")
    private Long modifiedUserId;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
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
     * 角色状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status" ,value="角色状态（0、不启用 1、启用）")
    private Byte status;

}