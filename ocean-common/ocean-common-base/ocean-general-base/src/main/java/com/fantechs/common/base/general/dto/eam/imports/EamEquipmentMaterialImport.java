package com.fantechs.common.base.general.dto.eam.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class EamEquipmentMaterialImport implements Serializable {

    /**
     * 设备编码(必填)
     */
    @Excel(name = "设备编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="equipmentCode" ,value="设备编码(必填)")
    private String equipmentCode;

    /**
     * 设备名称(必填)
     */
    @Excel(name = "设备名称(必填)", height = 20, width = 30)
    @ApiModelProperty(name="equipmentName" ,value="设备名称(必填)")
    private String equipmentName;

    /**
     * 设备描述
     */
    @Excel(name = "设备描述", height = 20, width = 30)
    @ApiModelProperty(name="equipmentDesc" ,value="设备描述")
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @Excel(name = "设备型号", height = 20, width = 30)
    @ApiModelProperty(name="equipmentModel" ,value="设备型号")
    private String equipmentModel;

    /**
     * 设备绑定产品表头ID
     */
    @ApiModelProperty(name="equipmentMaterialId" ,value="设备绑定产品表头ID")
    private Long equipmentMaterialId;

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
