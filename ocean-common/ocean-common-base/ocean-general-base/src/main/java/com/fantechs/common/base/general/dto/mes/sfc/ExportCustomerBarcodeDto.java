package com.fantechs.common.base.general.dto.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class ExportCustomerBarcodeDto implements Serializable {

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="1")
    private String barcode;

    /**
     * 条码状态(0-待投产 1-投产中 2-已完成 3-待打印)
     */
    @ApiModelProperty(name="barcodeStatus",value = "条码状态(0-待投产 1-投产中 2-已完成 3-待打印)")
    @Excel(name = "条码状态(0-待投产 1-投产中 2-已完成 3-待打印)", height = 20, width = 30,orderNum="2",replace = {"待投产_0", "投产中_1", "已完成_2","待打印_3"})
    @Column(name = "barcode_status")
    private Byte barcodeStatus;
}
