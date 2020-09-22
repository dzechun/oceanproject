package com.fantechs.common.base.dto.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel
public class SysRoleExcelDTO implements Serializable {

    private static final long serialVersionUID = 50379429607391022L;
    /**
     * 角色id
     */
    @ApiModelProperty(name="roleId" ,value="角色id")
    private String roleId;

    /**
     * 角色编码
     */
    @ApiModelProperty(name="roleCode" ,value="角色编码")
    @Excel(name = "角色编码", height = 20, width = 30)
    private String roleCode;

    /**
     * 角色名称
     */
    @ApiModelProperty(name="roleName" ,value="角色名称")
    @Excel(name = "角色名称", height = 20, width = 30)
    private String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty(name="roleDesc" ,value="角色描述")
    @Excel(name = "角色描述", height = 20, width = 30)
    private String roleDesc;

    /**
     * 创建账号
     */
    @ApiModelProperty(name="createUserId" ,value="创建用户")
    //@Excel(name = "创建用户", height = 20, width = 30)
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
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @ApiModelProperty(name="modifiedUserId" ,value="修改用户")
    // @Excel(name = "修改用户", height = 20, width = 30)
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
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
}
