package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class WmsInnerMaterialBarcodeReOrderDto extends WmsInnerMaterialBarcodeReOrder implements Serializable {

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Transient
    private String barcode;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料ID
     */
    @Transient
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 数量
     */
    @Transient
    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;

    /**
     * 生产日期
     */
    @Transient
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    /**
     * 彩盒号
     */
    @Transient
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    private String colorBoxCode;

    /**
     * 箱码
     */
    @Transient
    @ApiModelProperty(name="cartonCode",value = "箱码")
    private String cartonCode;

    /**
     * 栈板码
     */
    @Transient
    @ApiModelProperty(name="palletCode",value = "栈板码")
    private String palletCode;

    /**
     * 批次号
     */
    @Transient
    @ApiModelProperty(name="batchCode",value = "批次号")
    private String batchCode;

    /**
     * 是否系统条码
     */
    @Transient
    @ApiModelProperty(name="ifSysBarcode",value = "是否系统条码")
    private String ifSysBarcode;

    /**
     * 产生类型(1-供应商条码 2-自己打印 3-生产条码)
     */
    @Transient
    @ApiModelProperty(name="createType",value = "产生类型(1-供应商条码 2-自己打印 3-生产条码)")
    private Byte createType;

    /**
     * 条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)
     */
    @ApiModelProperty(name="barcodeType",value = "条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)")
    private Byte barcodeType;

    private static final long serialVersionUID = 1L;
}
