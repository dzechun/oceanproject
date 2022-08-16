package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInspectionStandardDet extends BaseQuery implements Serializable {

    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId" ,value="检验标准ID")
    private Long inspectionStandardId;


}
