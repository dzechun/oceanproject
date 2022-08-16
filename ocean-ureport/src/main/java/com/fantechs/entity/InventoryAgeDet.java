package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

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

    /**
     *  销售条码
     */
    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String  salesBarcode;

    /**
     *  客户条码
     */
    @ApiModelProperty(name = "customerBarcode",value = "客户条码")
    private String  customerBarcode;

    /**
     *  销售编码
     */
    @ApiModelProperty(name = "salesCode",value = "销售编码")
    private String  salesCode;

    /**
     *  PO号
     */
    @ApiModelProperty(name = "samePackageCode",value = "PO号")
    private String  samePackageCode;

    @ApiModelProperty(name="createTime",value = "创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
