package com.fantechs.common.base.general.dto.mes.sfc;


import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MesSfcProductPalletRePODto  implements Serializable {
    @ApiModelProperty(name="workOrderBarcodeId",value = "条码ID")
    @Id
    private Long workOrderBarcodeId;

    @ApiModelProperty(name="barcode",value = "厂内码")
    private String barcode;

    @ApiModelProperty(name="salesBarcode" ,value="销售条码")
    private String salesBarcode;

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
     * 同包装编码数量 same_package_code_qty
     */
    @ApiModelProperty(name="samePackageCodeQty",value = "同包装编码数量")
    private BigDecimal samePackageCodeQty;

    /**
     * 已匹配数量 matched_qty
     */
    @ApiModelProperty(name="matchedQty",value = "已匹配数量")
    private BigDecimal matchedQty;

    @ApiModelProperty(name="notMatchedQty",value = "新PO号剩余未匹配数")
    private BigDecimal notMatchedQty;

    @ApiModelProperty(name = "omSalesCodeReSpcList", value = "销售码对应PO集合")
    @Transient
    private List<OmSalesCodeReSpcDto> omSalesCodeReSpcList;

}
