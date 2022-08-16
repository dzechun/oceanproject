package com.fantechs.common.base.general.dto.eam.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class EamEquipmentJigImport implements Serializable {

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
     * 治具ID
     */
    @ApiModelProperty(name="equipmentJigId" ,value="治具ID")
    private Long equipmentJigId;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId" ,value="治具ID")
    private Long jigId;

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
     * 治具类别
     */
    @Excel(name = "治具类别", height = 20, width = 30)
    @ApiModelProperty(name="jigCategoryCode" ,value="治具类别")
    private String jigCategoryCode;

    /**
     * 使用数量(必填)
     */
    @Excel(name = "使用数量(必填)", height = 20, width = 30)
    @ApiModelProperty(name="usageQty",value = "使用数量(必填)")
    private Integer usageQty;


}
