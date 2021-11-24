package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Data
public class InventoryAgeDet implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(name = "inventoryDetId",value = "id")
    @Id
    private Long inventoryDetId;

    /**
     * 序号
     */
    @ApiModelProperty(name = "serialNumber",value = "序号")
    @Excel(name = "序号", height = 20, width = 30,orderNum="1")
    private int serialNumber;

    /**
     *条码
     */
    @ApiModelProperty(name = "barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="2")
    private String  barcode;

    /**
     * 条码状态(1-待收货、2-已收货、3-在库、4-已拣选、5-已复核、6-已出库、7-已取消)
     */
    @ApiModelProperty(name = "barcodeStatus",value = "条码状态(1-待收货、2-已收货、3-在库、4-已拣选、5-已复核、6-已出库、7-已取消)")
    @Excel(name = "条码状态", height = 20, width = 30,orderNum="3")
    private Byte barcodeStatus;
}
