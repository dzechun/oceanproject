package com.fantechs.common.base.general.dto.mes.sfc;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcKeyPartRelevance;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MesSfcBarcodeProcessDto extends MesSfcBarcodeProcess implements Serializable {

    @ApiModelProperty(name="sfcKeyPartRelevanceList",value = "附件码")
    private List<MesSfcKeyPartRelevance> sfcKeyPartRelevanceList;
}
