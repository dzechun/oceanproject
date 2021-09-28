package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

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
}
