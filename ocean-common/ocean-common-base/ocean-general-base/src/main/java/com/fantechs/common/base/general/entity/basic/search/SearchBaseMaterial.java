package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/15 11:20
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchBaseMaterial extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -4397503096388466915L;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 产品BOM ID
     */
    @ApiModelProperty(name="productBomId" ,value="产品BOM ID")
    private Long productBomId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * 物料属性查询标记（传1表示查询成品和半成品）
     */
    @ApiModelProperty(name = "propertyQueryMark",value = "物料属性查询标记（传1表示查询成品和半成品）")
    private Integer propertyQueryMark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    private Long organizationId;

    /**
     * 物料类别
     */
    @ApiModelProperty(name = "materialCategoryId",value = "物料类别")
    private Long materialCategoryId;

    /**
     * 是否启用配置项物料类别
     */
    @ApiModelProperty(name = "ifUseSpecMaterialCategory",value = "是否启用配置项物料类别")
    private Byte ifUseSpecMaterialCategory;

    /**
     * 物料属性(0.半成品，1.成品)
     */
    @ApiModelProperty(name = "materialProperty",value = "物料属性(0.半成品，1.成品)")
    private Integer materialProperty;

    private String idCode;

    private String option1;

    private String option2;

    private String option3;

    @ApiModelProperty(name="voltage" ,value="电压")
    private String voltage;

    @ApiModelProperty(name="brandName" ,value="品牌名称")
    private String brandName;

    @ApiModelProperty(name="productCategory" ,value="产品分类")
    private String productCategory;
}
