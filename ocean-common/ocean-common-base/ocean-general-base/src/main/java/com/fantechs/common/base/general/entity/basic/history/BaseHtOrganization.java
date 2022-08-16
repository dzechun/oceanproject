package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 组织履历信息表
 * base_ht_organization
 * @author 53203
 * @date 2020-12-29 18:10:08
 */
@Data
@Table(name = "base_ht_organization")
public class BaseHtOrganization implements Serializable {
    /**
     * 组织履历id
     */
    @ApiModelProperty(name="htOrganizationId",value = "组织履历id")
    @Excel(name = "组织履历id", height = 20, width = 30)
    @Id
    @Column(name = "ht_organization_id")
    private Long htOrganizationId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode",value = "组织编码")
    @Excel(name = "组织编码", height = 20, width = 30)
    @Column(name = "organization_code")
    private String organizationCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 组织描述
     */
    @ApiModelProperty(name="organizationDesc",value = "组织描述")
    @Excel(name = "组织描述", height = 20, width = 30)
    @Column(name = "organization_desc")
    private String organizationDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 创建人id
     */
    @ApiModelProperty(name="createUserId",value = "创建人id")
    @Excel(name = "创建人id", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人id
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人id")
    @Excel(name = "修改人id", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30)
    private Byte status;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}