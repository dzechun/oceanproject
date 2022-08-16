package com.fantechs.common.base.general.entity.basic.history;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_ht_factory")
@Data
public class BaseHtFactory implements Serializable {
    private static final long serialVersionUID = 7869934677097537670L;
    /**
     * id
     */
    @Id
    @Column(name = "ht_factory_id")
    private Long htFactoryId;

    @Column(name = "factory_id")
    @ApiModelProperty(name="factoryId" ,value="厂别id")
    private Long factoryId;

    /**
     * 厂别编码
     */
    @Column(name = "factory_code")
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    private String factoryCode;

    /**
     * 厂别名称
     */
    @Column(name = "factory_name")
    @ApiModelProperty(name="factoryName" ,value="厂别名称")
    private String factoryName;

    /**
     * 厂别描述
     */
    @Column(name = "factory_desc")
    @ApiModelProperty(name="factoryDesc" ,value="厂别描述")
    private String factoryDesc;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
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
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 工厂状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status" ,value="工厂状态（0、不启用 1、启用）")
    private Integer status;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    }
