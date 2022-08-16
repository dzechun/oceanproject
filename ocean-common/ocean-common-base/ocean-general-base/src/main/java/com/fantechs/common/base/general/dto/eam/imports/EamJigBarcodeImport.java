package com.fantechs.common.base.general.dto.eam.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class EamJigBarcodeImport implements Serializable {

    /**
     * 资产编码
     */
    @Excel(name = "资产编码",  height = 20, width = 30)
    @ApiModelProperty(name="assetCode" ,value="资产编码")
    private String assetCode;

    /**
     * 治具条码(必填)
     */
    @Excel(name = "治具条码(必填)", height = 20, width = 30)
    @ApiModelProperty(name="jigBarcode" ,value="治具条码(必填)")
    private String jigBarcode;

    /**
     * 已使用次数
     */
    @Excel(name = "已使用次数", height = 20, width = 30)
    @ApiModelProperty(name="currentUsageTime" ,value="已使用次数")
    private Integer currentUsageTime;

    /**
     * 已使用天数
     */
    @Excel(name = "已使用天数", height = 20, width = 30)
    @ApiModelProperty(name="currentUsageDays" ,value="已使用天数")
    private Integer currentUsageDays;
}
