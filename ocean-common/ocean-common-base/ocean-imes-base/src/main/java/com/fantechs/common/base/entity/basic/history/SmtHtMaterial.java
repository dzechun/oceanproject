package com.fantechs.common.base.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_ht_material")
@Data
public class SmtHtMaterial implements Serializable {
    private static final long serialVersionUID = -3766841188391585631L;
    /**
     * 物料履历ID
     */
    @Id
    @Column(name = "ht_material_id")
    private Long htMaterialId;

    /**
     * 物料ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @Column(name = "material_code")
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Column(name = "material_name")
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Column(name = "material_desc")
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;
    /**
     * 单位
     */
    @ApiModelProperty(name="unit" ,value="单位")
    private String unit;

    /**
     * 版本
     */
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

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
    @ApiModelProperty(name="productModelCode" ,value="产品型号")
    private String productModelCode;

    /**
     * 物料类别
     */
    @Column(name = "material_type")
    @ApiModelProperty(name="materialType" ,value="物料类别")
    private String materialType;

    /**
     * 物料来源
     */
    @Column(name = "material_source")
    @ApiModelProperty(name="materialSource" ,value="物料来源")
    private Integer materialSource;

    /**
     * 条码规则集合ID
     */
    @Column(name = "barcode_rule_id")
    @ApiModelProperty(name="barcodeRuleId" ,value="条码规则集合ID")
    private Long barcodeRuleId;

    /**
     * 条码规则集合ID
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleName" ,value="条码规则集合")
    private String barcodeRuleName;

    /**
     * 是否组合板(0.否 1.是)
     */
    @Column(name = "if_compoboard")
    @ApiModelProperty(name="ifCompoboard" ,value="是否组合板")
    private Integer ifCompoboard;

    /**
     * 是否连板(0.否 1.是)
     */
    @Column(name = "if_linking_board")
    @ApiModelProperty(name="ifLinkingBoard" ,value="是否连板")
    private Integer ifLinkingBoard;

    /**
     * 连板数
     */
    @Column(name = "linking_board_number")
    @ApiModelProperty(name="linkingBoardNumber" ,value="连板数")
    private Integer linkingBoardNumber;

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
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态")
    private Integer status;

    /**
     * 客户料号
     */
    @Column(name = "customer_material_code")
    @ApiModelProperty(name="customerMaterialCode" ,value="客户料号")
    private String customerMaterialCode;

    /**
     * 最小包装数
     */
    @Column(name = "min_package_number")
    @ApiModelProperty(name="minPackageNumber" ,value="最小包装数")
    private Integer minPackageNumber;

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
