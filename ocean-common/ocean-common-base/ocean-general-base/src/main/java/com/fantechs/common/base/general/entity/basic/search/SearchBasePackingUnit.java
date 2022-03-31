package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class SearchBasePackingUnit extends BaseQuery implements Serializable {

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName" ,value="包装单位名称")
    private String packingUnitName;

    /**
     * 包装单位描述
     */
    @ApiModelProperty(name="packingUnitDesc" ,value="包装单位描述")
    private String packingUnitDesc;

    /**
     * 是否主要(0否，1是)
     */
    @ApiModelProperty(name="isChief",value = "是否主要(0否，1是)")
    private String isChief;
}
