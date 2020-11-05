package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 包装规格表
 * smt_package_specification
 * @author 53203
 * @date 2020-11-04 16:47:58
 */
@Data
@Table(name = "smt_package_specification")
public class SmtPackageSpecification extends ValidGroup implements Serializable {
    /**
     * 包装规格ID
     */
    @ApiModelProperty(name="packageSpecificationId",value = "包装规格ID")
    @Id
    @Column(name = "package_specification_id")
    @NotNull(groups = update.class,message = "包装规格ID不能为空")
    private Long packageSpecificationId;

    /**
     * 包装规格编码
     */
    @ApiModelProperty(name="packageSpecificationCode",value = "包装规格编码")
    @Excel(name = "包装规格编码", height = 20, width = 30,orderNum="1")
    @Column(name = "package_specification_code")
    @NotNull(message = "包装规格编码不能为空")
    private String packageSpecificationCode;

    /**
     * 包装规格名称
     */
    @ApiModelProperty(name="packageSpecificationName",value = "包装规格名称")
    @Excel(name = "包装规格名称", height = 20, width = 30,orderNum="2")
    @Column(name = "package_specification_name")
    @NotNull(message = "包装规格名称不能为空")
    private String packageSpecificationName;

    /**
     * 包装规格描述
     */
    @ApiModelProperty(name="packageSpecificationDesc",value = "包装规格描述")
    @Excel(name = "包装规格描述", height = 20, width = 30,orderNum="3")
    @Column(name = "package_specification_desc")
    private String packageSpecificationDesc;

    /**
     * 包装规格数量
     */
    @ApiModelProperty(name="packageSpecificationQuantity",value = "包装规格数量")
    @Excel(name = "包装规格数量", height = 20, width = 30,orderNum="4")
    @Column(name = "package_specification_quantity")
    private BigDecimal packageSpecificationQuantity;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    @NotNull(message = "物料ID不能为空")
    private Long materialId;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则集合ID")
    @Excel(name = "条码规则集合ID", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_rule_id")
    @NotNull(message = "条码规则集合ID不能为空")
    private Long barcodeRuleId;

    /**
     * 包装单位ID
     */
    @ApiModelProperty(name="packingUnitId",value = "包装单位ID")
    @Excel(name = "包装单位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_unit_id")
    @NotNull(message = "包装单位ID不能为空")
    private Long packingUnitId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="7")
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="") 
    private String option3;

    private static final long serialVersionUID = 1L;
}