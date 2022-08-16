package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJig extends BaseQuery implements Serializable {

    /**
     * 治具类别ID
     */
    @ApiModelProperty(name="jigCategoryId",value = "治具类别ID")
    private Long jigCategoryId;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    private Long jigId;

    /**
     * 治具编码
     */
    @ApiModelProperty(name="jigCode",value = "治具编码")
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    private String jigName;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    private String jigModel;

    /**
     * 治具类别
     */
    @ApiModelProperty(name="jigCategoryName",value = "治具类别")
    private String jigCategoryName;


    /**
     * 治具条码
     */
    @ApiModelProperty(name="jigBarcode",value = "治具条码")
    private String jigBarcode;
}
