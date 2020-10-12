package com.fantechs.common.base.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_ht_route")
@Data
public class SmtHtRoute implements Serializable {

    private static final long serialVersionUID = 4845414832770781054L;
    /**
     * 工艺路线历史ID
     */
    @Id
    @Column(name = "ht_route_id")
    @ApiModelProperty(name="htRouteId" ,value="工艺路线历史ID")
    private Long htRouteId;

    /**
     * 工艺路线ID
     */
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线代码
     */
    @Column(name = "route_code")
    @ApiModelProperty(name="routeCode" ,value="工艺路线代码")
    private String routeCode;

    /**
     * 工艺路线名称
     */
    @Column(name = "route_name")
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 工艺路线描述
     */
    @Column(name = "route_desc")
    @ApiModelProperty(name="routeDesc" ,value="工艺路线描述")
    private String routeDesc;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态")
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
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
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
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
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除")
    private Byte isDelete;

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