package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInspectionStandard extends BaseQuery implements Serializable {

    /**
     * 检验标准编码
     */
    @ApiModelProperty(name="inspectionStandardCode" ,value="检验标准编码")
    private String inspectionStandardCode;

    /**
     * 检验标准名称
     */
    @ApiModelProperty(name="inspectionStandardName" ,value="检验标准名称")
    private String inspectionStandardName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 检验类型
     */
    @ApiModelProperty(name="inspectionTypeName" ,value="检验类型")
    private String inspectionTypeName;

}
