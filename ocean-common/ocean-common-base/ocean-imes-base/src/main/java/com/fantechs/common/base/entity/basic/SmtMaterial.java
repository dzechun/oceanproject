package com.fantechs.common.base.entity.basic;

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

@Table(name = "smt_material")
@Data
public class SmtMaterial extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 6879887744171421495L;
    /**
     * 物料ID
     */
    @Id
    @Column(name = "material_id")
    @NotNull(groups = update.class,message = "物料id不能为空")
    private Long materialId;

    /**
     * 物料编码
     */
    @Column(name = "material_code")
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @NotBlank(message = "物料编码不能为空")
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
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;
    /**
     * 单位
     */
    @ApiModelProperty(name="unit" ,value="单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;

    /**
     * 版本
     */
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30)
    private String version;

    /**
     *  产品型号ID
     */
    @Column(name = "product_model_id")
    @ApiModelProperty(name="productModelId" ,value="产品型号ID")
    @NotNull(message = "产品型号ID不能为空")
    private Long productModelId;


    /**
     * 物料类别
     */
    @Column(name = "material_type")
    @ApiModelProperty(name="materialType" ,value="物料类别")
    @Excel(name = "物料类别", height = 20, width = 30)
    private String materialType;

    /**
     * 物料来源(0.自制件 1.虚拟件 2.采购件)
     */
    @Column(name = "material_source")
    @ApiModelProperty(name="materialSource" ,value="物料来源(0.自制件 1.虚拟件 2.采购件)")
    @Excel(name = "物料来源", height = 20, width = 30,replace = {"自制件_0", "虚拟件_1","采购件_2"})
    private Integer materialSource;

    /**
     * 条码规则集合ID
     */
    @Column(name = "barcode_rule_id")
    @ApiModelProperty(name="barcodeRuleId" ,value="条码规则集合ID")
    private Long barcodeRuleId;


    /**
     * 是否组合板(0.否 1.是)
     */
    @Column(name = "if_compoboard")
    @ApiModelProperty(name="ifCompoboard" ,value="是否组合板")
    @Excel(name = "是否组合板", height = 20, width = 30,replace = {"否_0", "是_1"})
    private Integer ifCompoboard;

    /**
     * 是否连板(0.否 1.是)
     */
    @Column(name = "if_linking_board")
    @ApiModelProperty(name="ifLinkingBoard" ,value="是否连板")
    @Excel(name = "是否连板", height = 20, width = 30,replace = {"否_0", "是_1"})
    private Integer ifLinkingBoard;

    /**
     * 连板数
     */
    @Column(name = "linking_board_number")
    @ApiModelProperty(name="linkingBoardNumber" ,value="连板数")
    @Excel(name = "连板数", height = 20, width = 30)
    private Integer linkingBoardNumber;

    /**
     * 节拍数量(秒)
     */
    @Column(name = "takt")
    @ApiModelProperty(name="takt" ,value="节拍数量(秒)")
    private Integer takt;

    /**
     * 移转数量
     */
    @Column(name = "transfer_quantity")
    @ApiModelProperty(name="transferQuantity" ,value="移转数量")
    @NotNull(message = "移转数量不能为空")
    private Integer transferQuantity;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    @Column(name = "status")
    private Integer status;

    /**
     * 客户料号
     */
    @Column(name = "customer_material_code")
    @ApiModelProperty(name="customerMaterialCode" ,value="客户料号")
    @Excel(name = "客户料号", height = 20, width = 30)
    private String customerMaterialCode;

    /**
     * 最小包装数
     */
    @Column(name = "min_package_number")
    @ApiModelProperty(name="minPackageNumber" ,value="最小包装数")
    @Excel(name = "最小包装数", height = 20, width = 30)
    private Integer minPackageNumber;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号ID")
    private Long createUserId;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号ID")
    private Long modifiedUserId;


    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
    private Byte isDelete;
    /**
     * 扩展字段1
     */
    @Column(name = "option1")
    private String option1;

    /**
     * 扩展字段2
     */
    @Column(name = "option2")
    private String option2;

    /**
     * 扩展字段3
     */
    @Column(name = "option3")
    private String option3;

}
