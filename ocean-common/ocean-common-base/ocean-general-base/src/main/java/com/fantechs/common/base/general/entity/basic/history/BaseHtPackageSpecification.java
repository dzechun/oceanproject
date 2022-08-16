package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.dto.basic.BaseMaterialPackageDto;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

/**
 * 包装规格历史表
 * base_ht_package_specification
 * @author 53203
 * @date 2020-11-04 16:47:59
 */
@Data
@Table(name = "base_ht_package_specification")
public class BaseHtPackageSpecification implements Serializable {
    /**
     * 包装规格历史ID
     */
    @ApiModelProperty(name="htPackageSpecificationId",value = "包装规格历史ID")
    @Excel(name = "包装规格历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_package_specification_id")
    private Long htPackageSpecificationId;

    /**
     * 包装规格ID
     */
    @ApiModelProperty(name="packageSpecificationId",value = "包装规格ID")
    @Excel(name = "包装规格ID", height = 20, width = 30)
    @Column(name = "package_specification_id")
    private Long packageSpecificationId;

    /**
     * 包装规格编码
     */
    @ApiModelProperty(name="packageSpecificationCode",value = "包装规格编码")
    @Excel(name = "包装规格编码", height = 20, width = 30)
    @Column(name = "package_specification_code")
    private String packageSpecificationCode;

    /**
     * 包装规格名称
     */
    @ApiModelProperty(name="packageSpecificationName",value = "包装规格名称")
    @Excel(name = "包装规格名称", height = 20, width = 30)
    @Column(name = "package_specification_name")
    private String packageSpecificationName;

    /**
     * 包装规格描述
     */
    @ApiModelProperty(name="packageSpecificationDesc",value = "包装规格描述")
    @Excel(name = "包装规格描述", height = 20, width = 30)
    @Column(name = "package_specification_desc")
    private String packageSpecificationDesc;

    /**
     * 包装规格数量
     */
    @ApiModelProperty(name="packageSpecificationQuantity",value = "包装规格数量")
    @Excel(name = "包装规格数量", height = 20, width = 30)
    @Column(name = "package_specification_quantity")
    private BigDecimal packageSpecificationQuantity;

//    *
//     * 物料ID
//
//    @ApiModelProperty(name="materialId",value = "物料ID")
//    @Excel(name = "物料ID", height = 20, width = 30)
//    @Column(name = "material_id")
//    private Long materialId;
//
//    *
//     * 物料编码
//
//    @ApiModelProperty(name="materialCode" ,value="物料编码")
//    @Transient
//    @Excel(name = "物料编码", height = 20, width = 30)
//    private String materialCode;
//
//
//    *
//     * 物料描述
//
//    @ApiModelProperty(name="materialDesc" ,value="物料描述")
//    @Transient
//    @Excel(name = "物料描述", height = 20, width = 30)
//    private String materialDesc;
//
//    *
//     * 版本
//
//    @ApiModelProperty(name="materialVersion" ,value="版本")
//    @Transient
//    private String materialVersion;
//
//    *
//     * 条码规则集合ID
//
//    @ApiModelProperty(name="barcodeRuleId",value = "条码规则集合ID")
//    @Excel(name = "条码规则集合ID", height = 20, width = 30)
//    @Column(name = "barcode_rule_id")
//    private Long barcodeRuleId;
//
//    *
//     * 条码规则
//
//    @ApiModelProperty(name="barcodeRule",value = "条码规则")
//    @Excel(name = "条码规则", height = 20, width = 30,orderNum="13")
//    @Transient
//    private String barcodeRule;
//
//    *
//     * 包装单位ID
//
//    @ApiModelProperty(name="packingUnitId",value = "包装单位ID")
//    @Excel(name = "包装单位ID", height = 20, width = 30)
//    @Column(name = "packing_unit_id")
//    private Long packingUnitId;
//
//    *
//     * 包装单位名称
//
//    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
//    @Excel(name = "包装单位名称", height = 20, width = 30,orderNum="1")
//    @Transient
//    private String packingUnitName;
//
//    *
//     * 包装单位描述
//
//    @ApiModelProperty(name="packingUnitDesc",value = "包装单位描述")
//    @Excel(name = "包装单位描述", height = 20, width = 30,orderNum="2")
//    @Transient
//    private String packingUnitDesc;

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
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30)
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30)
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30)
    private String option3;

    /**
     * 包装规格物料关系集合
     */
    @ApiModelProperty(name="baseMaterialPackages",value = "包装规格物料关系集合")
    private List<BaseMaterialPackageDto> baseMaterialPackages = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}
