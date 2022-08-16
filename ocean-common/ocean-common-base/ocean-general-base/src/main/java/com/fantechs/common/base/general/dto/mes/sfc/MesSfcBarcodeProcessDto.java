package com.fantechs.common.base.general.dto.mes.sfc;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcKeyPartRelevance;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MesSfcBarcodeProcessDto extends MesSfcBarcodeProcess implements Serializable {

    @ApiModelProperty(name="productModelCode",value = "产品型号编码")
    private String productModelCode;

    @ApiModelProperty(name="productModelName",value = "产品型号名称")
    private String productModelName;

    @ApiModelProperty(name="productFamilyCode",value = "产品族编码")
    private String productFamilyCode;

    @ApiModelProperty(name="productFamilyName",value = "产品族名称")
    private String productFamilyName;


    @ApiModelProperty(name="sfcKeyPartRelevanceList",value = "附件码")
    private List<MesSfcKeyPartRelevance> sfcKeyPartRelevanceList;
}
