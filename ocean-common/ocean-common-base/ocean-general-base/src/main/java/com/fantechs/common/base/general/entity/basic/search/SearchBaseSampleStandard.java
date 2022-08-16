package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseSampleStandard extends BaseQuery implements Serializable {

    /**
     * 抽样标准名称
     */
    @ApiModelProperty(name="sampleStandardName",value = "抽样标准名称")
    private String sampleStandardName;

    /**
     * 抽样标准描述
     */
    @ApiModelProperty(name="sampleStandardDesc",value = "抽样标准描述")
    private String sampleStandardDesc;
}
