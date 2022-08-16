package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigBarcode extends BaseQuery implements Serializable {

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
     * 治具描述
     */
    @ApiModelProperty(name="jigDesc",value = "治具描述")
    private String jigDesc;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    private String jigModel;

    /**
     * 治具条码
     */
    @ApiModelProperty(name="jigBarcode",value = "治具条码")
    private String jigBarcode;

    /**
     * 资产条码
     */
    @ApiModelProperty(name="assetCode",value = "资产条码")
    private String assetCode;

    /**
     * 是否按治具id分组（0-否 1-是）
     */
    @ApiModelProperty(name="ifGroup",value = "是否按治具id分组（0-否 1-是）")
    private Integer ifGroup;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;
}
