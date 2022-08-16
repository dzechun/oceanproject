package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseProcessInspectionItem extends BaseQuery implements Serializable {
    /**
     * 检验项目编码
     */
    @ApiModelProperty(name="processInspectionItemCode" ,value="检验项目编码")
    private String processInspectionItemCode;

    /**
     * 检验项目描述
     */
    @ApiModelProperty(name="processInspectionItemDesc" ,value="检验项目描述")
    private String processInspectionItemDesc;

    /**
     * 检验类型
     */
    @ApiModelProperty(name="processInspectionItemType" ,value="检验类型")
    private Byte processInspectionItemType;

    /**
     * 产品料号id
     */
    @ApiModelProperty(name="materialId" ,value="产品料号id")
    private Long materialId;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;
}
