package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.Validation;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 页签信息表
 * base_tab
 * @author 53203
 * @date 2021-01-12 19:20:36
 */
@Data
@Table(name = "base_tab")
public class BaseTab extends ValidGroup implements Serializable {
    /**
     * 页签ID
     */
    @ApiModelProperty(name="tabId",value = "页签ID")
    @Excel(name = "页签ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "tab_id")
    @NotNull(groups = update.class,message = "页签ID不能为空")
    private Long tabId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 检验项目ID
     */
    @ApiModelProperty(name="inspectionItemId",value = "检验项目ID")
    @Excel(name = "检验项目ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_item_id")
    private Long inspectionItemId;

    /**
     * 包装规格ID
     */
    @ApiModelProperty(name="packageSpecificationId",value = "包装规格ID")
    @Excel(name = "包装规格ID", height = 20, width = 30,orderNum="") 
    @Column(name = "package_specification_id")
    private Long packageSpecificationId;

    /**
     * 产品型号id
     */
    @ApiModelProperty(name="productModelId",value = "产品型号id")
    @Excel(name = "产品型号id", height = 20, width = 30,orderNum="") 
    @Column(name = "product_model_id")
    private Long productModelId;

    /**
     * 标签类别id
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别id")
    @Excel(name = "标签类别id", height = 20, width = 30,orderNum="") 
    @Column(name = "label_category_id")
    private Long labelCategoryId;

    /**
     * 标签信息id
     */
    @ApiModelProperty(name="labelId",value = "标签信息id")
    @Excel(name = "标签信息id", height = 20, width = 30,orderNum="") 
    @Column(name = "label_id")
    private Long labelId;

    /**
     * 检验类型ID
     */
    @ApiModelProperty(name="inspectionTypeId",value = "检验类型ID")
    @Excel(name = "检验类型ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_type_id")
    private Long inspectionTypeId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 物料简称
     */
    @ApiModelProperty(name="simpleName",value = "物料简称")
    @Excel(name = "物料简称", height = 20, width = 30,orderNum="") 
    @Column(name = "simple_name")
    private String simpleName;

    /**
     * 英语描述
     */
    @ApiModelProperty(name="englishDescribe",value = "英语描述")
    @Excel(name = "英语描述", height = 20, width = 30,orderNum="") 
    @Column(name = "english_describe")
    private String englishDescribe;

    /**
     * 物料属性(0.半成品，1.成品)
     */
    @ApiModelProperty(name="materialProperty",value = "物料属性(0.半成品，1.成品)")
    @Excel(name = "物料属性(0.半成品，1.成品)", height = 20, width = 30,orderNum="") 
    @Column(name = "material_property")
    private Byte materialProperty;

    /**
     * 图片
     */
    @ApiModelProperty(name="image",value = "图片")
    @Excel(name = "图片", height = 20, width = 30,orderNum="") 
    private String image;

    /**
     * 是否批次(0.否 1.是)
     */
    @ApiModelProperty(name="isBatch",value = "是否批次(0.否 1.是)")
    @Excel(name = "是否批次(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_batch")
    private Byte isBatch;

    /**
     * 是否质量检查(0.否 1.是)
     */
    @ApiModelProperty(name="isQualityTest",value = "是否质量检查(0.否 1.是)")
    @Excel(name = "是否质量检查(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_quality_test")
    private Byte isQualityTest;

    /**
     * 是否箱码(0.否 1.是)
     */
    @ApiModelProperty(name="isCaseCode",value = "是否箱码(0.否 1.是)")
    @Excel(name = "是否箱码(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_case_code")
    private Byte isCaseCode;

    /**
     * 是否序列码(0.否 1.是)
     */
    @ApiModelProperty(name="isSequenceCode",value = "是否序列码(0.否 1.是)")
    @Excel(name = "是否序列码(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_sequence_code")
    private Byte isSequenceCode;

    /**
     * 发料方式(0.直领 1.倒冲)
     */
    @ApiModelProperty(name="issueMethod",value = "发料方式(0.直领 1.倒冲)")
    @Excel(name = "发料方式(0.直领 1.倒冲)", height = 20, width = 30,orderNum="") 
    @Column(name = "issue_method")
    private Byte issueMethod;

    /**
     * 是否组合板(0.否 1.是)
     */
    @ApiModelProperty(name="ifCompoboard",value = "是否组合板(0.否 1.是)")
    @Excel(name = "是否组合板(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "if_compoboard")
    private Byte ifCompoboard;

    /**
     * 是否连板(0.否 1.是)
     */
    @ApiModelProperty(name="ifLinkingBoard",value = "是否连板(0.否 1.是)")
    @Excel(name = "是否连板(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "if_linking_board")
    private Byte ifLinkingBoard;

    /**
     * 连板数
     */
    @ApiModelProperty(name="linkingBoardNumber",value = "连板数")
    @Excel(name = "连板数", height = 20, width = 30,orderNum="") 
    @Column(name = "linking_board_number")
    private Integer linkingBoardNumber;

    /**
     * 节拍数量(秒)
     */
    @ApiModelProperty(name="takt",value = "节拍数量(秒)")
    @Excel(name = "节拍数量(秒)", height = 20, width = 30,orderNum="") 
    private Integer takt;

    /**
     * 最小安全库存
     */
    @ApiModelProperty(name="minSafetyStock",value = "最小安全库存")
    @Excel(name = "最小安全库存", height = 20, width = 30,orderNum="") 
    @Column(name = "min_safety_stock")
    private Integer minSafetyStock;

    /**
     * 最大安全库存
     */
    @ApiModelProperty(name="maxSafetyStock",value = "最大安全库存")
    @Excel(name = "最大安全库存", height = 20, width = 30,orderNum="") 
    @Column(name = "max_safety_stock")
    private Integer maxSafetyStock;

    /**
     * 采购周期
     */
    @ApiModelProperty(name="purchaseCycle",value = "采购周期")
    @Excel(name = "采购周期", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_cycle")
    private Long purchaseCycle;

    /**
     * 移转数量
     */
    @ApiModelProperty(name="transferQuantity",value = "移转数量")
    @Excel(name = "移转数量", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_quantity")
    private Integer transferQuantity;

    /**
     * 主单位
     */
    @ApiModelProperty(name="mainUnit",value = "主单位")
    @Excel(name = "主单位", height = 20, width = 30,orderNum="") 
    @Column(name = "main_unit")
    private String mainUnit;

    /**
     * 辅单位
     */
    @ApiModelProperty(name="subUnit",value = "辅单位")
    @Excel(name = "辅单位", height = 20, width = 30,orderNum="") 
    @Column(name = "sub_unit")
    private String subUnit;

    /**
     * 转换率
     */
    @ApiModelProperty(name="conversionRate",value = "转换率")
    @Excel(name = "转换率", height = 20, width = 30,orderNum="") 
    @Column(name = "conversion_rate")
    private Integer conversionRate;

    /**
     * 客户料号
     */
    @ApiModelProperty(name="customerMaterialCode",value = "客户料号")
    @Excel(name = "客户料号", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_material_code")
    private String customerMaterialCode;

    /**
     * 供应物料方式(0.推式 1.拉式)
     */
    @ApiModelProperty(name="supplyMode",value = "供应物料方式(0.推式 1.拉式)")
    @Excel(name = "供应物料方式(0.推式 1.拉式)", height = 20, width = 30,orderNum="") 
    @Column(name = "supply_mode")
    private Integer supplyMode;

    /**
     * 颜色
     */
    @ApiModelProperty(name="color",value = "颜色")
    @Excel(name = "颜色", height = 20, width = 30,orderNum="") 
    private String color;

    /**
     * 长
     */
    @ApiModelProperty(name="length",value = "长")
    @Excel(name = "长", height = 20, width = 30,orderNum="") 
    private BigDecimal length;

    /**
     * 宽
     */
    @ApiModelProperty(name="width",value = "宽")
    @Excel(name = "宽", height = 20, width = 30,orderNum="") 
    private BigDecimal width;

    /**
     * 高
     */
    @ApiModelProperty(name="height",value = "高")
    @Excel(name = "高", height = 20, width = 30,orderNum="") 
    private BigDecimal height;

    /**
     * 体积
     */
    @ApiModelProperty(name="volume",value = "体积")
    @Excel(name = "体积", height = 20, width = 30,orderNum="") 
    private BigDecimal volume;

    /**
     * 净重
     */
    @ApiModelProperty(name="netWeight",value = "净重")
    @Excel(name = "净重", height = 20, width = 30,orderNum="") 
    @Column(name = "net_weight")
    private BigDecimal netWeight;

    /**
     * 毛重
     */
    @ApiModelProperty(name="grossWeight",value = "毛重")
    @Excel(name = "毛重", height = 20, width = 30,orderNum="") 
    @Column(name = "gross_weight")
    private BigDecimal grossWeight;

    /**
     * 物料类别
     */
    @ApiModelProperty(name="materialType",value = "物料类别")
    @Excel(name = "物料类别", height = 20, width = 30,orderNum="") 
    @Column(name = "material_type")
    private Byte materialType;

    /**
     * 采购批量
     */
    @ApiModelProperty(name="purchaseQuantity",value = "采购批量")
    @Excel(name = "采购批量", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_quantity")
    private BigDecimal purchaseQuantity;

    /**
     * 存储温度
     */
    @ApiModelProperty(name="storageTemperature",value = "存储温度")
    @Excel(name = "存储温度", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_temperature")
    private String storageTemperature;

    /**
     * 存储湿度
     */
    @ApiModelProperty(name="storageHumidity",value = "存储湿度")
    @Excel(name = "存储湿度", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_humidity")
    private String storageHumidity;

    /**
     * 存储要求备注
     */
    @ApiModelProperty(name="storageRequireDesc",value = "存储要求备注")
    @Excel(name = "存储要求备注", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_require_desc")
    private String storageRequireDesc;

    /**
     * 包装方式(0.箱 1.圈 2.桶)
     */
    @ApiModelProperty(name="packingMethod",value = "包装方式(0.箱 1.圈 2.桶)")
    @Excel(name = "包装方式(0.箱 1.圈 2.桶)", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_method")
    private Integer packingMethod;

    /**
     * 材质
     */
    @ApiModelProperty(name="materialQuality",value = "材质")
    @Excel(name = "材质", height = 20, width = 30,orderNum="")
    @Column(name = "material_quality")
    private String materialQuality;


    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 状态（0、否 1、是）
     */
    @ApiModelProperty(name="isWetSensitive",value = "状态（0、否 1、是）")
    @Excel(name = "状态（0、否 1、是）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_wet_sensitive")
    private Byte isWetSensitive;

    /**
     * 湿敏等级
     */
    @ApiModelProperty(name="wetSensitiveLevel",value = "湿敏等级")
    @Excel(name = "湿敏等级", height = 20, width = 30,orderNum="") 
    @Column(name = "wet_sensitive_level")
    private String wetSensitiveLevel;

    private static final long serialVersionUID = 1L;
}