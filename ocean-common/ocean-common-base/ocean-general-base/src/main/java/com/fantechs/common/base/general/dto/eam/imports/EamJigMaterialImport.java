package com.fantechs.common.base.general.dto.eam.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class EamJigMaterialImport implements Serializable {

    /**
     * 治具编码(必填)
     */
    @Excel(name = "治具编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="jigCode" ,value="治具编码(必填)")
    private String jigCode;

    /**
     * 治具名称(必填)
     */
    @Excel(name = "治具名称(必填)", height = 20, width = 30)
    @ApiModelProperty(name="jigName" ,value="治具名称(必填)")
    private String jigName;

    /**
     * 治具描述
     */
    @Excel(name = "治具描述", height = 20, width = 30)
    @ApiModelProperty(name="jigDesc" ,value="治具描述")
    private String jigDesc;

    /**
     * 治具型号
     */
    @Excel(name = "治具型号", height = 20, width = 30)
    @ApiModelProperty(name="jigModel" ,value="治具型号")
    private String jigModel;

    /**
     * 治具绑定产品ID
     */
    @ApiModelProperty(name="jigMaterialId" ,value="治具绑定产品ID")
    private Long jigMaterialId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码(必填)
     */
    @Excel(name = "物料编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="materialCode" ,value="物料编码(必填)")
    private String materialCode;

    /**
     * 名称(必填)
     */
    @Excel(name = "物料名称(必填)", height = 20, width = 30)
    @ApiModelProperty(name="materialName" ,value="物料名称(必填)")
    private String materialName;

    /**
     * 物料描述
     */
    @Excel(name = "物料描述", height = 20, width = 30)
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 物料版本
     */
    @Excel(name = "物料版本", height = 20, width = 30)
    @ApiModelProperty(name="materialVersion" ,value="物料版本")
    private String materialVersion;

    /**
     * 使用数量(必填)
     */
    @Excel(name = "使用数量(必填)", height = 20, width = 30)
    @ApiModelProperty(name="usageQty",value = "使用数量(必填)")
    private Integer usageQty;


}
