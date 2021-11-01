package com.fantechs.common.base.general.entity.kreport.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchLogisticsKanban extends BaseQuery implements Serializable {

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 承运商
     */
    @ApiModelProperty(name="carrierNames",value = "承运商")
    private List<String> carrierNames;

    /**
     * 单据类型
     */
    @ApiModelProperty(name="billTypeName",value = "单据类型")
    private List<String> billTypeNames;

    /**
     * 是否按照天查询(1、是 0、否)
     */
    @ApiModelProperty(name="isDay",value = "是否按照天查询(1、是 0、否)")
    private Byte isDay;


}
