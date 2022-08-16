package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 包装规格关联物料信息表
 * base_material_package
 * @author 53203
 * @date 2021-02-25 15:23:52
 */
@Data
@Table(name = "base_material_package")
public class BaseMaterialPackage extends ValidGroup implements Serializable {
    /**
     * 包装规格关联物料 
     */
    @ApiModelProperty(name="materialPackageId",value = "包装规格关联物料 ")
    @Excel(name = "包装规格关联物料 ", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "material_package_id")
    private Long materialPackageId;

    /**
     * 包装规格ID
     */
    @ApiModelProperty(name="packageSpecificationId",value = "包装规格ID")
    @Excel(name = "包装规格ID", height = 20, width = 30,orderNum="") 
    @Column(name = "package_specification_id")
    private Long packageSpecificationId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_id")
    private Long processId;

    /**
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    @Excel(name = "条码规则ID", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_rule_id")
    private Long barcodeRuleId;

    /**
     * 包装单位ID
     */
    @ApiModelProperty(name="packingUnitId",value = "包装单位ID")
    @Excel(name = "包装单位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_unit_id")
    private Long packingUnitId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}