package com.fantechs.common.base.general.entity.ureport;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SrmAsnOrder implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(name = "id",value = "id")
    @Id
    private Long id;

    /**
     * ASN单号
     */
    @ApiModelProperty(name = "asnOrderCode",value = "ASN单号")
    @Excel(name = "ASN单号", height = 20, width = 30,orderNum="1")
    private String asnOrderCode;

    /**
     * 采购订单号
     */
    @ApiModelProperty(name = "purchaseOrderCode",value = "采购订单号")
    @Excel(name = "采购订单号", height = 20, width = 30,orderNum="2")
    private String purchaseOrderCode;

    /**
     * 计划日期
     */
    @ApiModelProperty(name = "planDeliveryDate",value = "计划日期")
    @Excel(name = "计划日期", height = 20, width = 30,orderNum="3",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planDeliveryDate;

    /**
     *收货仓库
     */
    @ApiModelProperty(name = "warehouseName",value = "收货仓库")
    @Excel(name = "收货仓库", height = 20, width = 30,orderNum="4")
    private String warehouseName;

    /**
     *物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="5")
    private String  materialCode;

    /**
     *物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="6")
    private String  materialName;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="materialVersion",value = "物料版本")
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="7")
    private String materialVersion;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="8")
    private String materialDesc;

    /**
     *单位
     */
    @ApiModelProperty(name = "unitName",value = "单位")
    @Excel(name = "单位", height = 20, width = 30,orderNum="9")
    private String  unitName;

    /**
     * 采购数量
     */
    @ApiModelProperty(name="orderQty",value = "采购数量")
    @Excel(name = "采购数量", height = 20, width = 30,orderNum="10")
    private BigDecimal orderQty;

    /**
     * 累计交货数量
     */
    @ApiModelProperty(name="actualQty" ,value="累计交货数量")
    @Excel(name = "累计交货数量", height = 20, width = 30,orderNum="11")
    private BigDecimal actualQty;


    /**
     *SN码
     */
    @ApiModelProperty(name = "barcode",value = "SN码")
    @Excel(name = "SN码", height = 20, width = 30,orderNum="12")
    private String  barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30,orderNum="13")
    private String colorBoxCode;

    /**
     * 箱码
     */
    @ApiModelProperty(name="cartonCode" ,value="箱码")
    @Excel(name = "箱码", height = 20, width = 30,orderNum="14")
    private String cartonCode;

    /**
     *栈板码
     */
    @ApiModelProperty(name = "palletCode",value = "栈板码")
    @Excel(name = "栈板码", height = 20, width = 30,orderNum="15")
    private String  palletCode;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="16")
    private BigDecimal qty;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode" ,value="批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="17")
    private String batchCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="18",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date productionDate;

    /**
     *发货人
     */
    @ApiModelProperty(name = "shipperName",value = "发货人")
    @Excel(name = "发货人", height = 20, width = 30,orderNum="19")
    private String  shipperName;

    /**
     * 发货日期
     */
    @ApiModelProperty(name="deliverDate",value = "发货日期")
    @Excel(name = "发货日期", height = 20, width = 30,orderNum="20",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deliverDate;

    /**
     * ASN状态
     */
    @ApiModelProperty(name="orderStatus" ,value="ASN状态")
    @Excel(name = "ASN状态", height = 20, width = 30,orderNum="21",replace = {"保存_1","提交_2","审核通过_3","审核未通过该_4","已预约_5","发货_6"})
    private Byte orderStatus;

}
