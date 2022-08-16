package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseMaterialImport implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码(必填)", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称", height = 20, width = 30)
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @Excel(name = "外购产品料号", height = 20, width = 30)
    @ApiModelProperty(name="option1" ,value="外购产品料号")
    private String option1;

    @Excel(name = "电压", height = 20, width = 30)
    @ApiModelProperty(name="voltage" ,value="电压")
    private String voltage;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 移转批量
     */
    @ApiModelProperty(name="transferQuantity",value = "移转批量")
    @Excel(name = "移转批量", height = 20, width = 30)
    private Integer transferQuantity;

    /**
     * 版本
     */
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30)
    private String version;

    /**
     * 基数
     */
    @ApiModelProperty(name="base" ,value="基数")
    @Excel(name = "基数", height = 20, width = 30)
    private Integer base;

    /**
     * 物料来源(0.自制件 1.虚拟件 2.采购件)
     */
    @ApiModelProperty(name="materialSource" ,value="物料来源(0.自制件 1.虚拟件 2.采购件)")
    @Excel(name = "物料来源(0.自制件 1.虚拟件 2.采购件)", height = 20, width = 30)
    private Integer materialSource;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId" ,value="条码规则集合ID")
    private Long barcodeRuleSetId;

    /**
     * 条码规则集合编码
     */
    @Excel(name = "条码规则集合编码", height = 20, width = 30)
    @ApiModelProperty(name="barcodeRuleSetCode" ,value="条码规则集合编码")
    private String barcodeRuleSetCode;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Integer status;

    /**
     * 最小包装数
     */
    @Excel(name = "最小包装数", height = 20, width = 30)
    @ApiModelProperty(name="minPackageNumber" ,value="最小包装数")
    private Integer minPackageNumber;

    /**
     * 检验项目ID
     */
    @ApiModelProperty(name="inspectionItemId",value = "检验项目ID")
    private Long inspectionItemId;

    /**
     * 检验项目单号
     */
    @Excel(name = "检验项目单号", height = 20, width = 30)
    @ApiModelProperty(name="inspectionItemCode",value = "检验项目单号")
    private String inspectionItemCode;

    /**
     * 包装规格ID
     */
    @ApiModelProperty(name="packageSpecificationId",value = "包装规格ID")
    private Long packageSpecificationId;

    /**
     * 包装规格编码
     */
    @Excel(name = "包装规格编码", height = 20, width = 30)
    @ApiModelProperty(name="packageSpecificationCode",value = "包装规格编码")
    private String packageSpecificationCode;

    /**
     * 产品型号id
     */
    @ApiModelProperty(name="productModelId",value = "产品型号id")
    private Long productModelId;

    /**
     *  产品型号编码
     */
    @Excel(name = "产品型号编码", height = 20, width = 30)
    @ApiModelProperty(name="productModelCode",value = "产品型号编码")
    private String productModelCode;

    /**
     * 标签类别id
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别id")
    private Long labelCategoryId;

    /**
     * 标签类别编码
     */
    @ApiModelProperty(name="labelCategoryCode",value = "标签类别编码")
    @Excel(name = "标签类别编码", height = 20, width = 30)
    private String labelCategoryCode;

    /**
     * 标签信息id
     */
    @ApiModelProperty(name="labelId",value = "标签信息id")
    private Long labelId;

    /**
     * 标签代码
     */
    @ApiModelProperty(name="labelCode",value = "标签代码")
    @Excel(name = "标签代码", height = 20, width = 30)
    private String labelCode;

    /**
     * 检验类型ID
     */
    @ApiModelProperty(name="inspectionTypeId",value = "检验类型ID")
    private Long inspectionTypeId;

    /**
     * 检验类型编码
     */
    @Excel(name = "检验类型编码", height = 20, width = 30)
    @ApiModelProperty(name="inspectionTypeCode",value = "检验类型编码")
    private String inspectionTypeCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    private Long supplierId;

    /**
     * 供应商代码
     */
    @ApiModelProperty("供应商(客户)代码")
    @Excel(name = "供应商代码", height = 20, width = 30)
    private String supplierCode;

    /**
     * 物料简称
     */
    @ApiModelProperty(name="simpleName",value = "物料简称")
    @Excel(name = "物料简称", height = 20, width = 30)
    private String simpleName;

    /**
     * 英语描述
     */
    @ApiModelProperty(name="englishDescribe",value = "英语描述")
    @Excel(name = "英语描述", height = 20, width = 30)
    private String englishDescribe;

    /**
     * 品牌名称
     */
    @ApiModelProperty(name="brandName",value = "品牌名称")
    @Excel(name = "品牌名称", height = 20, width = 30)
    private String brandName;

    /**
     * 产品分类
     */
    @ApiModelProperty(name="productCategory",value = "产品分类")
    @Excel(name = "产品分类", height = 20, width = 30)
    private String productCategory;

    /**
     * 物料属性(0.半成品，1.成品)
     */
    @ApiModelProperty(name="materialProperty",value = "物料属性(0.半成品，1.成品)")
    @Excel(name = "物料属性(0.半成品，1.成品)", height = 20, width = 30)
    private Integer materialProperty;

    /**
     * 图片
     */
    @ApiModelProperty(name="image",value = "图片")
    @Excel(name = "图片", height = 20, width = 30)
    private String image;

    /**
     * 是否批次(0.否 1.是)
     */
    @ApiModelProperty(name="isBatch",value = "是否批次(0.否 1.是)")
    @Excel(name = "是否批次(0.否 1.是)", height = 20, width = 30)
    private Integer isBatch;

    /**
     * 是否质量检查(0.否 1.是)
     */
    @ApiModelProperty(name="isQualityTest",value = "是否质量检查(0.否 1.是)")
    @Excel(name = "是否质量检查(0.否 1.是)", height = 20, width = 30)
    private Integer isQualityTest;

    /**
     * 是否箱码(0.否 1.是)
     */
    @ApiModelProperty(name="isCaseCode",value = "是否箱码(0.否 1.是)")
    @Excel(name = "是否箱码(0.否 1.是)", height = 20, width = 30)
    private Integer isCaseCode;

    /**
     * 是否序列码(0.否 1.是)
     */
    @ApiModelProperty(name="isSequenceCode",value = "是否序列码(0.否 1.是)")
    @Excel(name = "是否序列码(0.否 1.是)", height = 20, width = 30)
    private Integer isSequenceCode;

    /**
     * 发料方式(0.直领 1.倒冲)
     */
    @ApiModelProperty(name="issueMethod",value = "发料方式(0.直领 1.倒冲)")
    @Excel(name = "发料方式(0.直领 1.倒冲)", height = 20, width = 30)
    private Integer issueMethod;

    /**
     * 是否组合板(0.否 1.是)
     */
    @ApiModelProperty(name="ifCompoboard",value = "是否组合板(0.否 1.是)")
    @Excel(name = "是否组合板(0.否 1.是)", height = 20, width = 30)
    private Integer ifCompoboard;

    /**
     * 是否连板(0.否 1.是)
     */
    @ApiModelProperty(name="ifLinkingBoard",value = "是否连板(0.否 1.是)")
    @Excel(name = "是否连板(0.否 1.是)", height = 20, width = 30)
    private Integer ifLinkingBoard;

    /**
     * 连板数
     */
    @ApiModelProperty(name="linkingBoardNumber",value = "连板数")
    @Excel(name = "连板数", height = 20, width = 30)
    private Integer linkingBoardNumber;

    /**
     * 节拍数量(秒)
     */
    @ApiModelProperty(name="takt",value = "节拍数量(秒)")
    @Excel(name = "节拍数量(秒)", height = 20, width = 30)
    private Integer takt;

    /**
     * 最小安全库存
     */
    @ApiModelProperty(name="minSafetyStock",value = "最小安全库存")
    @Excel(name = "最小安全库存", height = 20, width = 30)
    private Integer minSafetyStock;

    /**
     * 最大安全库存
     */
    @ApiModelProperty(name="maxSafetyStock",value = "最大安全库存")
    @Excel(name = "最大安全库存", height = 20, width = 30)
    private Integer maxSafetyStock;

    /**
     * 采购周期
     */
    @ApiModelProperty(name="purchaseCycle",value = "采购周期")
    @Excel(name = "采购周期", height = 20, width = 30)
    private Long purchaseCycle;

    /**
     * 主单位
     */
    @ApiModelProperty(name="mainUnit",value = "主单位")
    @Excel(name = "主单位", height = 20, width = 30)
    private String mainUnit;

    /**
     * 辅单位
     */
    @ApiModelProperty(name="subUnit",value = "辅单位")
    @Excel(name = "辅单位", height = 20, width = 30)
    private String subUnit;

    /**
     * 转换率
     */
    @ApiModelProperty(name="conversionRate",value = "转换率")
    @Excel(name = "转换率", height = 20, width = 30)
    private Integer conversionRate;

    /**
     * 客户料号
     */
    @ApiModelProperty(name="customerMaterialCode",value = "客户料号")
    @Excel(name = "客户料号", height = 20, width = 30)
    private String customerMaterialCode;

    /**
     * 供应物料方式(0.推式 1.拉式)
     */
    @ApiModelProperty(name="supplyMode",value = "供应物料方式(0.推式 1.拉式)")
    @Excel(name = "供应物料方式(0.推式 1.拉式)", height = 20, width = 30)
    private Integer supplyMode;

    /**
     * 颜色
     */
    @ApiModelProperty(name="color",value = "颜色")
    @Excel(name = "颜色", height = 20, width = 30)
    private String color;

    /**
     * 长
     */
    @ApiModelProperty(name="length",value = "长")
    @Excel(name = "长", height = 20, width = 30)
    private BigDecimal length;

    /**
     * 宽
     */
    @ApiModelProperty(name="width",value = "宽")
    @Excel(name = "宽", height = 20, width = 30)
    private BigDecimal width;

    /**
     * 高
     */
    @ApiModelProperty(name="height",value = "高")
    @Excel(name = "高", height = 20, width = 30)
    private BigDecimal height;

    /**
     * 体积
     */
    @ApiModelProperty(name="volume",value = "体积")
    @Excel(name = "体积", height = 20, width = 30)
    private BigDecimal volume;

    /**
     * 净重
     */
    @ApiModelProperty(name="netWeight",value = "净重")
    @Excel(name = "净重", height = 20, width = 30)
    private BigDecimal netWeight;

    /**
     * 毛重
     */
    @ApiModelProperty(name="grossWeight",value = "毛重")
    @Excel(name = "毛重", height = 20, width = 30)
    private BigDecimal grossWeight;

    /**
     * 物料类别
     */
    @ApiModelProperty(name="materialType",value = "物料类别")
    @Excel(name = "物料类别", height = 20, width = 30)
    private Integer materialType;

    /**
     * 采购批量
     */
    @ApiModelProperty(name="purchaseQuantity",value = "采购批量")
    @Excel(name = "采购批量", height = 20, width = 30)
    private BigDecimal purchaseQuantity;

    /**
     * 存储温度
     */
    @ApiModelProperty(name="storageTemperature",value = "存储温度")
    @Excel(name = "存储温度", height = 20, width = 30)
    private String storageTemperature;

    /**
     * 存储湿度
     */
    @ApiModelProperty(name="storageHumidity",value = "存储湿度")
    @Excel(name = "存储湿度", height = 20, width = 30)
    private String storageHumidity;

    /**
     * 存储要求备注
     */
    @ApiModelProperty(name="storageRequireDesc",value = "存储要求备注")
    @Excel(name = "存储要求备注", height = 20, width = 30)
    private String storageRequireDesc;

    /**
     * 包装方式(0.箱 1.圈 2.桶)
     */
    @ApiModelProperty(name="packingMethod",value = "包装方式(0.箱 1.圈 2.桶)")
    @Excel(name = "包装方式(0.箱 1.圈 2.桶)", height = 20, width = 30)
    private Integer packingMethod;

    /**
     * 材质
     */
    @ApiModelProperty(name="materialQuality",value = "材质")
    @Excel(name = "材质", height = 20, width = 30)
    private String materialQuality;

    /**
     * 是否湿敏（0、否 1、是）
     */
    @ApiModelProperty(name="isWetSensitive",value = "是否湿敏（0、否 1、是）")
    @Excel(name = "是否湿敏（0、否 1、是）", height = 20, width = 30)
    private Integer isWetSensitive;

    /**
     * 湿敏等级
     */
    @ApiModelProperty(name="wetSensitiveLevel",value = "湿敏等级")
    @Excel(name = "湿敏等级", height = 20, width = 30)
    private String wetSensitiveLevel;


}
