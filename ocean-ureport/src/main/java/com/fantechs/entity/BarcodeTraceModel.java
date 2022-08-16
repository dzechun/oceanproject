package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author huangshuijun
 * @Date 2021/9/29
 */
@Data
public class BarcodeTraceModel implements Serializable {
    @Id
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "work_order_id")
    private String workOrderId;

    @ApiModelProperty(name="orgId",value = "组织ID")
    @Column(name = "org_id")
    @Excel(name = "组织ID", height = 20, width = 30,orderNum="1")
    private String orgId;

    @ApiModelProperty(name="workOrderCode",value = "离散任务号-工单号")
    @Column(name = "work_order_code")
    @Excel(name = "离散任务号", height = 20, width = 30,orderNum="2")
    private String workOrderCode;

    @ApiModelProperty(name="salesOrderCode",value = "生产单号-销售单号")
    @Column(name = "sales_order_code")
    @Excel(name = "生产单号", height = 20, width = 30,orderNum="3")
    private String salesOrderCode;

    @ApiModelProperty(name="materialCode",value = "制造编码-物料编码")
    @Column(name = "material_code")
    @Excel(name = "制造编码", height = 20, width = 30,orderNum="4")
    private String materialCode;

    @ApiModelProperty(name="materialName",value = "产品描述")
    @Column(name = "material_name")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="5")
    private String materialName;

    @ApiModelProperty(name="barcode",value = "厂内码")
    @Column(name = "barcode")
    private String barcode;

    @ApiModelProperty(name="saleBarcode",value = "销售码")
    @Column(name = "sale_barcode")
    @Excel(name = "销售码", height = 20, width = 30,orderNum="6")
    private String saleBarcode;

    @ApiModelProperty(name="workCreateTime",value = "厂内码生成时间")
    @Column(name = "work_create_time")
    @Excel(name = "厂内码生成时间", height = 20, width = 30,orderNum="7")
    private String workCreateTime;

    @ApiModelProperty(name="saleCreateTime",value = "销售码生成时间")
    @Column(name = "sale_create_time")
    @Excel(name = "销售码生成时间", height = 20, width = 30,orderNum="8")
    private String saleCreateTime;

    @ApiModelProperty(name="workPrintTime",value = "厂内码打印时间")
    @Column(name = "work_print_time")
    @Excel(name = "厂内码打印时间", height = 20, width = 30,orderNum="9")
    private String workPrintTime;

    @ApiModelProperty(name="salePrintTime",value = "销售码打印时间")
    @Column(name = "sale_print_time")
    @Excel(name = "销售码打印时间", height = 20, width = 30,orderNum="10")
    private String salePrintTime;

    @ApiModelProperty(name="cartonScanTime",value = "打包扫描时间")
    @Column(name = "carton_scan_time")
    @Excel(name = "打包扫描时间", height = 20, width = 30,orderNum="11")
    private String cartonScanTime;

    @ApiModelProperty(name="palletScanTime",value = "入库下线扫描时间")
    @Column(name = "pallet_scan_time")
    @Excel(name = "入库下线扫描时间", height = 20, width = 30,orderNum="12")
    private String palletScanTime;

    @ApiModelProperty(name="receivingDate",value = "堆垛入库时间")
    @Column(name = "receiving_date")
    @Excel(name = "堆垛入库时间", height = 20, width = 30,orderNum="13")
    private String receivingDate;

    @ApiModelProperty(name="deliverDate",value = "拣货出库时间")
    @Column(name = "deliver_date")
    @Excel(name = "拣货出库时间", height = 20, width = 30,orderNum="14")
    private String deliverDate;

    @ApiModelProperty(name="carScanTime",value = "装车扫描时间")
    @Column(name = "car_scan_time")
    @Excel(name = "装车扫描时间", height = 20, width = 30,orderNum="15")
    private String carScanTime;

    @ApiModelProperty(name="barcodeStatus",value = "条码状态")
    @Column(name = "barcode_status")
    @Excel(name = "条码状态", height = 20, width = 30,orderNum="16")
    private String barcodeStatus;
}
