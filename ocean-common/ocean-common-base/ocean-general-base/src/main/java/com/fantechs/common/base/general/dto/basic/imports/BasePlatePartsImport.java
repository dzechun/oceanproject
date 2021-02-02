package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BasePlatePartsImport implements Serializable {

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    @Excel(name = "产品编码(必填)", height = 20, width = 30, orderNum = "0")
    private String materialCode;

    /**
     * 部件编码
     */
    @ApiModelProperty(name="partsInformationCode",value = "部件编码")
    @Excel(name = "部件编码", height = 20, width = 30, orderNum = "1")
    private String partsInformationCode;

    /**
     * 部件规格
     */
    @ApiModelProperty(name="specification",value = "部件规格")
    @Excel(name = "部件规格", height = 20, width = 30, orderNum = "2")
    private String specification;

    /**
     * 部件单位
     */
    @ApiModelProperty(name="unit",value = "部件单位")
    @Excel(name = "部件单位", height = 20, width = 30, orderNum = "3")
    private String unit;

    /**
     * 部件用量
     */
    @ApiModelProperty(name="quantity",value = "部件用量")
    @Excel(name = "部件用量", height = 20, width = 30, orderNum = "4")
    private String quantity;

    /**
     * 部件颜色
     */
    @ApiModelProperty(name="color",value = "部件颜色")
    @Excel(name = "部件颜色", height = 20, width = 30, orderNum = "5")
    private String color;

    /**
     * 部件材质
     */
    @ApiModelProperty(name="texture",value = "部件材质")
    @Excel(name = "部件材质", height = 20, width = 30, orderNum = "6")
    private String materialQuality;

    /**
     * 工艺路线编码
     */
    @ApiModelProperty(name="routeCode",value = "工艺路线编码")
    @Excel(name = "工艺路线编码", height = 20, width = 30, orderNum = "7")
    private String routeCode;

    /**
     * 是否是定制产品（0、否 1、是）
     */
    @ApiModelProperty(name="ifCustomized",value = "是否是定制产品（0、否 1、是）")
    @Excel(name = "是否是定制产品（0、否 1、是）", height = 20, width = 30, orderNum = "8")
    private String ifCustomized;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode",value = "组织编码")
    @Excel(name = "组织编码", height = 20, width = 30,orderNum="9")
    private String organizationCode;

}
