package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PDAWmsInnerSplitAndCombineCartonPalletInfoDto implements Serializable {

    /**
     * 包箱/栈板信息
     */
    @ApiModelProperty(name="cartonPalletInventoryDetDto",value = "包箱/栈板信息")
    private WmsInnerInventoryDetDto cartonPalletInventoryDetDto;

    /**
     * 包箱/栈板下级条码
     */
    @ApiModelProperty(name="nextLevelInventoryDetDtos",value = "包箱/栈板下级条码")
    private List<WmsInnerInventoryDetDto> nextLevelInventoryDetDtos;


}
