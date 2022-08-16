package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class BaseWarehouseAreaImport implements Serializable {

    /**
     * 库区编码(必填)
     */
    @ApiModelProperty(name = "warehouseAreaCode",value = "库区编码(必填)")
    @Excel(name = "库区编码(必填)", height = 20, width = 30)
    private String warehouseAreaCode;

    /**
     * 库区名称(必填)
     */
    @ApiModelProperty(name = "warehouseAreaName",value = "库区名称(必填)")
    @Excel(name = "库区名称(必填)", height = 20, width = 30)
    private String warehouseAreaName;

    /**
     * 库区描述
     */
    @ApiModelProperty(name = "warehouseAreaDesc",value = "库区描述")
    @Excel(name = "库区描述", height = 20, width = 30)
    private String warehouseAreaDesc;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit" ,value="单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 容量
     */
    @ApiModelProperty(name="capacity",value = "容量")
    @Excel(name = "容量", height = 20, width = 30)
    private BigDecimal capacity;

    /**
     * 温度
     */
    @ApiModelProperty(name="temperature",value = "温度")
    @Excel(name = "温度", height = 20, width = 30)
    private BigDecimal temperature;

    /**
     * 所属仓库编码(必填)
     */
    @Excel(name = "所属仓库编码(必填)", height = 20, width = 30)
    @ApiModelProperty(name="warehouseCode" ,value="所属仓库编码(必填)")
    private String warehouseCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

}
