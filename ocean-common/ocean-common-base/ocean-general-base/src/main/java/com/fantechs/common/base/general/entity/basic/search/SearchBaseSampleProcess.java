package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseSampleProcess extends BaseQuery implements Serializable {

    /**
     * 抽样过程编码
     */
    @ApiModelProperty(name="sampleProcessCode" ,value="抽样过程编码")
    private String sampleProcessCode;

    /**
     * 抽样过程描述
     */
    @ApiModelProperty(name="sampleProcessDesc" ,value="抽样过程描述")
    private String sampleProcessDesc;

    /**
     * 抽样类型(1-固定抽样 2-全检 3-百分比抽样 4-抽样方案)
     */
    @ApiModelProperty(name="sampleProcessType" ,value="抽样类型(1-固定抽样 2-全检 3-百分比抽样 4-抽样方案)")
    private Byte sampleProcessType;

    /**
     * 抽样过程id
     */
    @ApiModelProperty(name = "sampleProcessId", value = "抽样过程id")
    private Long sampleProcessId;
}
