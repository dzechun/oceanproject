package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class SearchBasePackageSpecification extends BaseQuery implements Serializable{

    /**
     * 包装规格编码
     */
    @ApiModelProperty(name="packageSpecificationCode",value = "包装规格编码")
    private String packageSpecificationCode;

    /**
     * 包装规格名称
     */
    @ApiModelProperty(name="packageSpecificationName",value = "包装规格名称")
    private String packageSpecificationName;

    /**
     * 包装规格描述
     */
    @ApiModelProperty(name="packageSpecificationDesc",value = "包装规格描述")
    private String packageSpecificationDesc;

    /**
     * 包装单位ID
     */
    @ApiModelProperty(name="packingUnitId",value = "包装单位ID")
    private Long packingUnitId;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    private String packingUnitName;

    /**
     * 包装单位描述
     */
    @ApiModelProperty(name="packingUnitDesc",value = "包装单位描述")
    private String packingUnitDesc;

    /**
     * 物料Id
     */
    @ApiModelProperty(name="materialId" ,value="物料Id")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 工序Id
     */
    @ApiModelProperty(name="processId" ,value="工序Id")
    private Long processId;
}
