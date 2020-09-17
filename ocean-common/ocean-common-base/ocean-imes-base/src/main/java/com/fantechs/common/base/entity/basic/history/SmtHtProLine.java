package com.fantechs.common.base.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_ht_pro_line")
@Data
public class SmtHtProLine implements Serializable {
    /**
     * 线别履历ID
     */
    @Id
    @Column(name = "ht_pro_line_id")
    private Long htProLineId;

    /**
     * 线别ID
     */
    @Column(name = "pro_line_id")
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private Long proLineId;
    /**
     * 线别代码
     */
    @Column(name = "pro_code")
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private String proCode;

    /**
     * 线别名称
     */
    @Column(name = "pro_name")
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private String proName;

    /**
     * 线别描述
     */
    @Column(name = "pro_desc")
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private String proDesc;

    /**
     * 厂别ID
     */
    @Column(name = "factory_id")
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private Long factoryId;

    /**
     * 厂别名称
     */
    @Transient
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private String factoryName;

    /**
     * 车间ID
     */
    @Column(name = "work_shop_id")
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private Long workShopId;

    /**
     * 车间名称
     */
    @Transient
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private String workShopName;

    /**
     * 产线状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private Integer status;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    private Date modifiedTime;

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
}