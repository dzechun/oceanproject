package com.fantechs.common.base.general.dto.om.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OmPurchaseReturnOrderImport implements Serializable {

    /**
     * 组号(必填)
     */
    @Excel(name = "组号(必填)", height = 20, width = 30)
    @ApiModelProperty(name="groupNum" ,value="组号(必填)")
    private String groupNum;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId" ,value="供应商ID")
    private Long supplierId;

    /**
     * 供应商编码
     */
    @Excel(name = "供应商编码(必填)", height = 20, width = 30)
    @ApiModelProperty(name="supplierCode" ,value="供应商编码")
    private String supplierCode;

    /**
     * 制单人ID
     */
    @ApiModelProperty(name="makeOrderUserId" ,value="制单人ID")
    private Long makeOrderUserId;

    /**
     * 制单人编码
     */
    @Excel(name = "制单人编码(必填)", height = 20, width = 30)
    @ApiModelProperty(name="makeOrderUserCode" ,value="制单人编码")
    private String makeOrderUserCode;

    /**
     * 退货部门ID
     */
    @ApiModelProperty(name="returnDeptId" ,value="退货部门ID")
    private Long returnDeptId;

    /**
     * 退货部门编码
     */
    @Excel(name = "退货部门编码", height = 20, width = 30)
    @ApiModelProperty(name="returnDeptCode" ,value="退货部门编码")
    private String returnDeptCode;

    /**
     * 备注
     */
    @Excel(name = "备注", height = 20, width = 30)
    @ApiModelProperty(name="remark" ,value="备注")
    private String remark;


    //明细字段=====================================================================

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", height = 20, width = 30)
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 包装数量
     */
    @ApiModelProperty(value = "orderQty",example = "包装数量")
    @Excel(name = "包装数量", height = 20, width = 30)
    private BigDecimal orderQty;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId" ,value="仓库ID")
    private Long warehouseId;

    /**
     * 仓库编码
     */
    @Excel(name = "仓库编码", height = 20, width = 30)
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    private String warehouseCode;


}
