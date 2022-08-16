package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

/**
 * 组织信息表
 * base_organization
 * @author 53203
 * @date 2020-12-29 18:10:07
 */
@Data
@Table(name = "base_organization")
public class BaseOrganization extends ValidGroup implements Serializable {
    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Id
    @Column(name = "organization_id")
    @NotNull(groups = update.class,message = "组织id不能为空")
    private Long organizationId;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode",value = "组织编码")
    @Excel(name = "组织编码", height = 20, width = 30,orderNum="1")
    @Column(name = "organization_code")
    @NotBlank(message = "组织编码不能为空")
    private String organizationCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum="2")
    @Column(name = "organization_name")
    @NotBlank(message = "组织名称不能为空")
    private String organizationName;

    /**
     * 组织描述
     */
    @ApiModelProperty(name="organizationDesc",value = "组织描述")
    @Excel(name = "组织描述", height = 20, width = 30,orderNum="3")
    @Column(name = "organization_desc")
    private String organizationDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30,orderNum="4",replace = {"不启用_0","启用_1"})
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="5",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}