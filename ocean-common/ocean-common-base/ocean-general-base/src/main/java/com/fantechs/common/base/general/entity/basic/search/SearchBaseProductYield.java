package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/1 10:10
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchBaseProductYield extends BaseQuery implements Serializable {

    /**
     * 良率类别(1-通用 2-产线 3-料号)
     */
    @ApiModelProperty(name="yieldType",value = "良率类别(1-通用 2-产线 3-料号)")
    private Byte yieldType;


    /**
     * 产线名称
     */
    @ApiModelProperty(name="proName",value = "产线名称")
    private String proName;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 产线id
     */
    @ApiModelProperty(name="proLineId",value = "产线id")
    private Long proLineId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialCode",value = "物料id")
    private String materialId;
}
