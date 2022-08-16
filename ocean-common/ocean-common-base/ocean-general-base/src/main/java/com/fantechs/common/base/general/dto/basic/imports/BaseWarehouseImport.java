package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class BaseWarehouseImport implements Serializable {

    /**
     * 仓库编码(必填)
     */
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码(必填)")
    @Excel(name = "仓库编码(必填)", height = 20, width = 30)
    private String warehouseCode;

    /**
     * 仓库名称(必填)
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称(必填)")
    @Excel(name = "仓库名称(必填)", height = 20, width = 30)
    private String warehouseName;

    /**
     * 仓库描述
     */
    @ApiModelProperty(name = "warehouseDesc",value = "仓库描述")
    @Excel(name = "仓库描述", height = 20, width = 30)
    private String warehouseDesc;

    /**
     * 仓库类型(0、未知类型 1、普通仓 2、辅料仓 3、暂存仓 4、成品仓 5、原材料仓 6、线边仓)
     */
    @ApiModelProperty(name = "warehouseCategory",value = "仓库类型(0、未知类型 1、普通仓 2、辅料仓 3、暂存仓 4、成品仓 5、原材料仓 6、线边仓)")
    @Excel(name = "仓库类型(0、未知类型 1、普通仓 2、辅料仓 3、暂存仓 4、成品仓 5、原材料仓 6、线边仓)", height = 20, width = 30)
    private Long warehouseCategory;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit" ,value="单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;

    /**
     * 联系人
     */
    @ApiModelProperty(name="linkManName",value = "联系人")
    @Excel(name = "联系人", height = 20, width = 30)
    private String linkManName;

    /**
     * 联系电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系电话")
    @Excel(name = "联系电话", height = 20, width = 30)
    private String linkManPhone;

    /**
     * 传真
     */
    @ApiModelProperty(name="faxNumber",value = "传真")
    @Excel(name = "传真", height = 20, width = 30)
    private String faxNumber;

    /**
     * 地址
     */
    @ApiModelProperty(name="address",value = "地址")
    @Excel(name = "地址", height = 20, width = 30)
    private String address;

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
     * 是否参与齐套分析(0.否 1.是)
     */
    @ApiModelProperty(name="completeAnalysis",value = "是否参与齐套分析(0.否 1.是)")
    @Excel(name = "是否参与齐套分析(0.否 1.是)", height = 20, width = 30)
    private Integer completeAnalysis;

    /**
     * 是否参与MRB运算(0.否 1.是)
     */
    @ApiModelProperty(name="MrbOperation",value = "是否参与齐套分析(0.否 1.是)")
    @Excel(name = "是否参与MRB运算(0.否 1.是)", height = 20, width = 30)
    private Integer mrbOperation;

    /**
     * 货主编码
     */
    @Excel(name = "货主编码", height = 20, width = 30)
    @ApiModelProperty(name="materialOwnerCode" ,value="货主编码")
    private String materialOwnerCode;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    private Long materialOwnerId;

}
