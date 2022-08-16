package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BaseProductProcessRouteImport implements Serializable {

    /**
     * 类别(0.All(*) 1.线别名称 2.产品型号 3.产品料号)
     */
    @ApiModelProperty(name="productType" ,value="类别(0.All(*) 1.线别名称 2.产品型号 3.产品料号)")
    @Excel(name = "类别(0.All(*) 1.线别名称 2.产品型号 3.产品料号)(必填)", height = 20, width = 30)
    private Integer productType;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线编码
     */
    @ApiModelProperty(name="routeCode" ,value="工艺路线编码")
    @Excel(name = "工艺路线编码(必填)", height = 20, width = 30)
    private String routeCode;

    /**
     * 线别ID
     */
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private Long proLineId;

    /**
     * 线别编码
     */
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "线别编码", height = 20, width = 30)
    private String proCode;

    /**
     *  产品型号ID
     */
    @ApiModelProperty(name="productModelId" ,value="产品型号ID")
    private Long productModelId;

    /**
     *  产品型号编码
     */
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Excel(name = "产品型号编码", height = 20, width = 30)
    private String productModelCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
