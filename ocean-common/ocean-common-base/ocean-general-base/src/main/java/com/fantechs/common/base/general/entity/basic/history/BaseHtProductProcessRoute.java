package com.fantechs.common.base.general.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "base_ht_product_process_route")
@Data
public class BaseHtProductProcessRoute {
    /**
     * 产品工艺路线历史ID
     */
    @Id
    @Column(name = "ht_product_process_route_id")
    private Long htProductProcessRouteId;

    /**
     * 产品工艺路线ID
     */
    @Column(name = "product_process_route_id")
    @ApiModelProperty(name="proCode" ,value="线别代码")
    private Long productProcessRouteId;

    /**
     * 类别(0.All(*) 1.线别名称 2.产品型号 3.产品料号)
     */
    @Column(name = "product_type")
    @ApiModelProperty(name="productType" ,value="类别")
    private Integer productType;

    /**
     *  名称
     */
    @Transient
    @ApiModelProperty(name="productName" ,value="名称")
    private String productName;


    /**
     * 线别ID
     */
    @Column(name = "pro_line_id")
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private Long proLineId;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;
    /**
     *  产品型号ID
     */
    @Column(name = "product_model_id")
    @ApiModelProperty(name="productModelId" ,value="产品型号ID")
    private Long productModelId;

    /**
     *  产品型号编码
     */
    @Transient
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    private String productModelCode;

    /**
     *  产品型号名称
     */
    @Transient
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    private String productModelName;

    /**
     *  物料ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    /**
     * 工艺路线ID
     */
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线编码
     */
    @Transient
    @ApiModelProperty(name="routeCode" ,value="工艺路线编码")
    private String routeCode;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeDesc" ,value="工艺路线名称")
    private String routeDesc;

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
