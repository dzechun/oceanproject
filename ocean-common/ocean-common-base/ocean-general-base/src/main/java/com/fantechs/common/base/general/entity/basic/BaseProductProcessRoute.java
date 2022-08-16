package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_product_process_route")
@Data
public class BaseProductProcessRoute extends ValidGroup implements Serializable {
    /**
     * 产品工艺路线ID
     */
    @Id
    @Column(name = "product_process_route_id")
    @ApiModelProperty(name="proCode" ,value="线别代码")
    @NotNull(groups = update.class,message = "id不能为空")
    private Long productProcessRouteId;

    /**
     * 类别(0.All(*) 1.线别名称 2.产品型号 3.产品料号)
     */
    @Column(name = "product_type")
    @ApiModelProperty(name="productType" ,value="类别(0.All(*) 1.线别名称 2.产品型号 3.产品料号)")
    @Excel(name = "类别", height = 20, width = 30,replace = {"All(*)_0", "线别_1","产品型号_2","产品料号_3"})
    @NotNull(message = "类别号不能为空")
    private Integer productType;

    /**
     * 产品名称
     */
    @Transient
    @ApiModelProperty(name="productName" ,value="名称")
    @Excel(name = "产品名称", height = 20, width = 30)
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
     * 线别编码
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proCode;

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
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Excel(name = "产品型号", height = 20, width = 30)
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
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 产品料号版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 产品料号描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;


    /**
     * 工艺路线ID
     */
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    @NotNull(message = "工艺路线id不能为空")
    private Long routeId;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30)
    private String routeName;

    /**
     * 工艺路线编码
     */
    @Transient
    @ApiModelProperty(name="routeCode" ,value="工艺路线编码")
    @Excel(name = "工艺路线编码", height = 20, width = 30)
    private String routeCode;

    /**
     * 工艺路线描述
     */
    @Transient
    @ApiModelProperty(name="routeDesc" ,value="工艺路线描述")
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
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
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
