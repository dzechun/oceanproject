package com.fantechs.entity;

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
    private String orgId;

    @ApiModelProperty(name="workOrderCode",value = "离散任务号-工单号")
    @Column(name = "work_order_code")
    private String workOrderCode;

    @ApiModelProperty(name="salesOrderCode",value = "生产单号-销售单号")
    @Column(name = "sales_order_code")
    private String salesOrderCode;

    @ApiModelProperty(name="materialCode",value = "制造编码-物料编码")
    @Column(name = "material_code")
    private String materialCode;

    @ApiModelProperty(name="materialName",value = "产品描述")
    @Column(name = "material_name")
    private String materialName;

    @ApiModelProperty(name="barcode",value = "厂内码")
    @Column(name = "barcode")
    private String barcode;

    @ApiModelProperty(name="saleBarcode",value = "销售码")
    @Column(name = "sale_barcode")
    private String saleBarcode;

    @ApiModelProperty(name="workCreateTime",value = "厂内码生成时间")
    @Column(name = "work_create_time")
    private String workCreateTime;

    @ApiModelProperty(name="saleCreateTime",value = "销售码生成时间")
    @Column(name = "sale_create_time")
    private String saleCreateTime;

    @ApiModelProperty(name="workPrintTime",value = "厂内码打印时间")
    @Column(name = "work_print_time")
    private String workPrintTime;

    @ApiModelProperty(name="salePrintTime",value = "销售码打印时间")
    @Column(name = "sale_print_time")
    private String salePrintTime;

    @ApiModelProperty(name="cartonScanTime",value = "打包扫描时间")
    @Column(name = "carton_scan_time")
    private String cartonScanTime;

    @ApiModelProperty(name="palletScanTime",value = "入库下线扫描时间")
    @Column(name = "pallet_scan_time")
    private String palletScanTime;

    @ApiModelProperty(name="receivingDate",value = "堆垛入库时间")
    @Column(name = "receiving_date")
    private String receivingDate;

    @ApiModelProperty(name="deliverDate",value = "拣货出库时间")
    @Column(name = "deliver_date")
    private String deliverDate;

    @ApiModelProperty(name="carScanTime",value = "装车扫描时间")
    @Column(name = "car_scan_time")
    private String carScanTime;

    @ApiModelProperty(name="barcodeStatus",value = "条码状态")
    @Column(name = "barcode_status")
    private String barcodeStatus;
}
