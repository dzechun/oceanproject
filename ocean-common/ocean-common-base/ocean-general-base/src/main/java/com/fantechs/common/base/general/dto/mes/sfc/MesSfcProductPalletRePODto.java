package com.fantechs.common.base.general.dto.mes.sfc;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesSfcProductPalletRePODto  implements Serializable {

    @ApiModelProperty(name="barcode",value = "厂内码")
    private String barcode;

    @ApiModelProperty(name="salesCode" ,value="销售码")
    private String salesCode;

    @ApiModelProperty(name="oldSamePackageCode" ,value="原PO号")
    private String oldSamePackageCode;

    @ApiModelProperty(name="newSamePackageCode",value = "新PO号")
    private String newSamePackageCode;

    @ApiModelProperty(name="materialCode",value = "产品编号")
    private String materialCode;

    @ApiModelProperty(name="materialName",value = "产品描述")
    private String materialName;

    /**
     * 同包装编码数量
     */
    @ApiModelProperty(name="samePackageCodeQty",value = "同包装编码数量")
    @Column(name = "same_package_code_qty")
    private BigDecimal samePackageCodeQty;

    /**
     * 已匹配数量
     */
    @ApiModelProperty(name="matchedQty",value = "已匹配数量")
    @Column(name = "matched_qty")
    private BigDecimal matchedQty;

    @ApiModelProperty(name="notMatchedQty",value = "新PO号剩余未匹配数")
    private BigDecimal notMatchedQty;


}
