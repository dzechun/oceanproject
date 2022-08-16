package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchBaseBadnessPhenotype extends BaseQuery implements Serializable {

    /**
     * 不良现象代码
     */
    @ApiModelProperty(name="badnessPhenotypeCode",value = "不良现象代码")
    private String badnessPhenotypeCode;

    /**
     * 不良现象描述
     */
    @ApiModelProperty(name="badnessPhenotypeDesc",value = "不良现象描述")
    private String badnessPhenotypeDesc;

}
