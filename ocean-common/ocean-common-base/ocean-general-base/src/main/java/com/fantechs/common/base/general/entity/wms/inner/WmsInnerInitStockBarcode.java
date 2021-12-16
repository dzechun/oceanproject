package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

;
;

/**
 * wms_inner_init_stock_barcode
 * @author mr.lei
 * @date 2021-12-01 10:02:20
 */
@Data
@Table(name = "wms_inner_init_stock_barcode")
public class WmsInnerInitStockBarcode extends ValidGroup implements Serializable {
    /**
     * 初始化条码id
     */
    @ApiModelProperty(name="initStockBarcodeId",value = "初始化条码id")
    @Excel(name = "初始化条码id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "init_stock_barcode_id")
    private Long initStockBarcodeId;

    /**
     * 盘点单id
     */
    @ApiModelProperty(name="initStockId",value = "盘点单id")
    @Excel(name = "盘点单id", height = 20, width = 30,orderNum="")
    @Column(name = "init_stock_id")
    private Long initStockId;

    /**
     * 明细id
     */
    @ApiModelProperty(name="initStockDetId",value = "明细id")
    @Excel(name = "明细id", height = 20, width = 30,orderNum="") 
    @Column(name = "init_stock_det_id")
    private Long initStockDetId;

    /**
     * 厂内码
     */
    @ApiModelProperty(name="inPlantBarcode",value = "厂内码")
    @Excel(name = "厂内码", height = 20, width = 30,orderNum="") 
    @Column(name = "in_plant_barcode")
    private String inPlantBarcode;

    /**
     * 销售条码
     */
    @ApiModelProperty(name="salesBarcode",value = "销售条码")
    @Excel(name = "销售条码", height = 20, width = 30,orderNum="") 
    @Column(name = "sales_barcode")
    private String salesBarcode;

    /**
     * 客户条码1
     */
    @ApiModelProperty(name="clientBarcode1",value = "客户条码1")
    @Excel(name = "客户条码1", height = 20, width = 30,orderNum="") 
    @Column(name = "client_barcode1")
    private String clientBarcode1;

    /**
     * 客户条码2
     */
    @ApiModelProperty(name="clientBarcode2",value = "客户条码2")
    @Excel(name = "客户条码2", height = 20, width = 30,orderNum="") 
    @Column(name = "client_barcode2")
    private String clientBarcode2;

    /**
     * 客户条码3
     */
    @ApiModelProperty(name="clientBarcode3",value = "客户条码3")
    @Excel(name = "客户条码3", height = 20, width = 30,orderNum="") 
    @Column(name = "client_barcode3")
    private String clientBarcode3;

    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    private static final long serialVersionUID = 1L;
}