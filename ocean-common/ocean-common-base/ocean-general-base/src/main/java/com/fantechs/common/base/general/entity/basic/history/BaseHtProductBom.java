package com.fantechs.common.base.general.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_ht_product_bom")
@Data
public class BaseHtProductBom implements Serializable {
    private static final long serialVersionUID = -6995052097724646232L;
    /**
     * 产品BOM历史ID
     */
    @Id
    @Column(name = "ht_product_bom_id")
    @ApiModelProperty(name="htProductBomId" ,value="产品BOM历史ID")
    private Long htProductBomId;

    /**
     * 产品BOM ID
     */
    @Column(name = "product_bom_id")
    @ApiModelProperty(name="productBomId" ,value="产品BOM ID")
    private Long productBomId;

    /**
     * 物料清单编号
     */
    @Column(name = "product_bom_code")
    @ApiModelProperty(name="productBomCode" ,value="BOM ID")
    private String productBomCode;

    /**
     * 产品料号ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
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
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 节拍数量(秒)
     */
    @ApiModelProperty(name="takt" ,value="节拍数量(秒)")
    private Integer takt;

    /**
     * 移转数量
     */
    @Column(name = "transfer_quantity")
    @ApiModelProperty(name="transferQuantity" ,value="移转数量")
    private Integer transferQuantity;

    /**
     * BOM状态(1-未核准 2-已核准)
     */
    @Column(name = "bom_status")
    @ApiModelProperty(name="bomStatus" ,value="BOM状态(1-未核准 2-已核准)")
    private Byte bomStatus;

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
    @ApiModelProperty(name="status" ,value="状态（0、无效 1、有效）")
    private Integer status;

    /**
     * 父BOM ID
     */
    @Column(name = "parent_bom_id")
    @ApiModelProperty(name="materialId" ,value="父BOM ID")
    private Long parentBomId;

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
