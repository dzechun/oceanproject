package com.fantechs.common.base.general.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "base_ht_product_bom_det")
@Data
public class BaseHtProductBomDet implements Serializable {
    private static final long serialVersionUID = -6576340839230471944L;
    /**
     * 产品BOM详细历史ID
     */
    @Id
    @Column(name = "ht_product_bom_det_id")
    @ApiModelProperty(name="htProductBomDetId" ,value="产品BOM详细历史ID")
    private Long htProductBomDetId;

    /**
     * 产品BOM详细ID
     */
    @Column(name = "product_bom_det_id")
    @ApiModelProperty(name="productBomDetId" ,value="产品BOM详细ID")
    private Long productBomDetId;

    /**
     * 产品BOM ID
     */
    @Column(name = "product_bom_id")
    @ApiModelProperty(name="productBomId" ,value="产品BOM ID")
    private Long productBomId;

    /**
     * 物料ID
     */
    @Column(name = "part_material_id")
    @ApiModelProperty(name="partMaterialId" ,value="物料ID")
    private Long partMaterialId;

    /**
     * 零件料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="零件料号")
    private String partMaterialCode;

    /**
     * 零件名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="零件名称")
    private String materialName;

    /**
     * 零件料号版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="零件料号版本")
    private String materialVersion;

    /**
     * 零件料号描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="零件料号描述")
    private String materialDesc;


    /**
     * 代用物料ID
     */
    @Column(name = "sub_material_id")
    @ApiModelProperty(name="subMaterialId" ,value="代用物料ID")
    private Long subMaterialId;

    /**
     * 代用料号
     */
    @Transient
    @ApiModelProperty(name="subMaterialCode" ,value="代用料号")
    private String subMaterialCode;

    /**
     * 工序ID
     */
    @Column(name = "process_id")
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;
    /**
     * 用量
     */
    @ApiModelProperty(name="quantity" ,value="用量")
    private BigDecimal quantity;

    /**
     * 基准数量
     */
    @ApiModelProperty(name="baseQuantity" ,value="基准数量")
    private BigDecimal baseQuantity;

    /**
     * 位置
     */
    @ApiModelProperty(name="position" ,value="位置")
    private String position;

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
