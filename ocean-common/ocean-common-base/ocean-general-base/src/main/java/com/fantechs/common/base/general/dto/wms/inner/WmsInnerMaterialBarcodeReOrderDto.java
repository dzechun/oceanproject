package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class WmsInnerMaterialBarcodeReOrderDto extends WmsInnerMaterialBarcodeReOrder implements Serializable {
    /**
     * 编码
     */
    @Transient
    @ApiModelProperty(name="code",value = "编码")
    private String code;

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


    private static final long serialVersionUID = 1L;
}