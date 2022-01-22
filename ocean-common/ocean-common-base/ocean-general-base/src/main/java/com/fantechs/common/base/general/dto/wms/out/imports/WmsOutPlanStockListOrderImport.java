package com.fantechs.common.base.general.dto.wms.out.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsOutPlanStockListOrderImport implements Serializable {

    /**
     * 组号(必填)
     */
    @Excel(name = "组号(必填)", height = 20, width = 30)
    @ApiModelProperty(name="groupNum" ,value="组号(必填)")
    private String groupNum;

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

    /**
     * 备注
     */
    @Excel(name = "备注", height = 20, width = 30)
    @ApiModelProperty(name="remark" ,value="备注")
    private String remark;


    //明细字段=====================================================================

    /**
     * 生产订单
     */
    @Excel(name = "生产订单", height = 20, width = 30)
    @ApiModelProperty(name="workOrderCode" ,value="生产订单")
    private String workOrderCode;

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
     * 订单数量
     */
    @ApiModelProperty(value = "orderQty",example = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30)
    private BigDecimal orderQty;

}
