package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInspectionWay extends BaseQuery implements Serializable {

    /**
     * 检验方式编码
     */
    @ApiModelProperty(name="inspectionWayCode" ,value="检验方式编码")
    private String inspectionWayCode;

    /**
     * 检验方式描述
     */
    @ApiModelProperty(name="inspectionWayDesc" ,value="检验方式描述")
    private String inspectionWayDesc;

    /**
     * 检验类型
     */
    @ApiModelProperty(name="inspectionTypeName" ,value="检验类型")
    private String inspectionTypeName;


}
