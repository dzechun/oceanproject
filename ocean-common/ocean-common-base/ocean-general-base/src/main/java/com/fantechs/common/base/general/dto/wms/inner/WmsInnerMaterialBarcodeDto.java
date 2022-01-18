package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WmsInnerMaterialBarcodeDto extends WmsInnerMaterialBarcode implements Serializable {

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQty",value = "订单数量")
    @Transient
    private BigDecimal orderQty;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Transient
    private BigDecimal qty;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    /**
     * 打印张数
     */
    @ApiModelProperty(name="printQty",value = "打印张数")
    @Transient
    private int printQty;

    /**
     * 生成数量
     */
    @ApiModelProperty(name="generateQty",value = "生成数量")
    @Transient
    private BigDecimal generateQty;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId" ,value="条码规则集合ID")
    @Transient
    private Long barcodeRuleSetId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Transient
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="3")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Transient
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="4")
    private String materialName;


    /**
     * 已打印物料总数量
     */
    @ApiModelProperty(name="totalMaterialQty",value = "已打印物料总数量")
    @Transient
    private BigDecimal totalMaterialQty;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商", height = 20, width = 30,orderNum="2")
    private String supplierName;


    private static final long serialVersionUID = 1L;
}
