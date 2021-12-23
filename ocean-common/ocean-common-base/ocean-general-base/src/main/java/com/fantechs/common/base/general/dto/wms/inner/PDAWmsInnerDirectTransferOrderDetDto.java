package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PDAWmsInnerDirectTransferOrderDetDto implements Serializable {


    /**
     * 扫描数量
     */
    @ApiModelProperty(name="qty",value = "扫描数量")
    private BigDecimal qty;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;


    private static final long serialVersionUID = 1L;
}
