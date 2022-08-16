package com.fantechs.common.base.general.dto.eam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 通过设备条码获取点检项目配置信息
 */
@Data
public class EamEquMaintainOrderDto implements Serializable {


    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    private String equipmentName;

    /**
     * 设备类别名称
     */
    @ApiModelProperty(name="equipmentCategoryName",value = "设备类别名称")
    private String equipmentCategoryName;

    /**
     * 设备保养编码
     */
    @ApiModelProperty(name="equipmentMaintainProjectCode",value = "设备点检编码")
    private String equipmentMaintainProjectCode;

    /**
     * 设备保养名称
     */
    @ApiModelProperty(name="equipmentMaintainProjectName",value = "设备点检名称")
    private String equipmentMaintainProjectName;

    /**
     * 单据状态(1-待保养 2-已保养)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待保养 2-已保养)")
    private Byte orderStatus;

    /**
     * 保养项目事项集合
     */
    @Transient
    @ApiModelProperty(name = "items",value = "保养项目事项集合")
    private List<EamEquipmentMaintainProjectItemDto> items;
}
