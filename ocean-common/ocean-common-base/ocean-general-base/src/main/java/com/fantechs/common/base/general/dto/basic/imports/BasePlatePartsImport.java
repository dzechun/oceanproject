package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BasePlatePartsImport implements Serializable {

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId",value = "产品ID")
    private Long materialId;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    @Excel(name = "产品编码(必填)", height = 20, width = 30)
    private String materialCode;

    /**
     * 部件ID
     */
    @ApiModelProperty(name="partsInformationId",value = "部件ID")
    private Long partsInformationId;

    /**
     * 部件编码
     */
    @ApiModelProperty(name="partsInformationCode",value = "部件编码")
    @Excel(name = "部件编码(必填)", height = 20, width = 30)
    private String partsInformationCode;

    /**
     * 部件规格
     */
    @ApiModelProperty(name="specification",value = "部件规格")
    @Excel(name = "部件规格", height = 20, width = 30)
    private String specification;

    /**
     * 部件单位
     */
    @ApiModelProperty(name="unit",value = "部件单位")
    @Excel(name = "部件单位", height = 20, width = 30)
    private String unit;

    /**
     * 部件用量
     */
    @ApiModelProperty(name="quantity",value = "部件用量")
    @Excel(name = "部件用量", height = 20, width = 30)
    private BigDecimal quantity;

    /**
     * 部件颜色
     */
    @ApiModelProperty(name="color",value = "部件颜色")
    @Excel(name = "部件颜色", height = 20, width = 30)
    private String color;

    /**
     * 部件材质
     */
    @ApiModelProperty(name="texture",value = "部件材质")
    @Excel(name = "部件材质", height = 20, width = 30)
    private String materialQuality;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    private Long routeId;

//    /**
//     * 工艺路线编码
//     */
//    @ApiModelProperty(name="routeCode",value = "工艺路线编码")
//    @Excel(name = "工艺路线编码(必填)", height = 20, width = 30)
//    private String routeCode;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty(name="routeName",value = "工艺路线名称")
    @Excel(name = "工艺路线名称(必填)", height = 20, width = 30)
    private String routeName;

    /**
     * 是否是定制产品（0、否 1、是）
     */
    @ApiModelProperty(name="ifCustomized",value = "是否是定制产品（0、否 1、是）")
    @Excel(name = "是否是定制产品（0、否 1、是）", height = 20, width = 30)
    private String ifCustomized;

}
