package com.fantechs.common.base.general.dto.eam;

import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProjectItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 通过设备条码获取点检项目配置信息
 */
@Data
public class EamEquInspectionOrderDto implements Serializable {


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
     * 设备点检编码
     */
    @ApiModelProperty(name="equPointInspectionProjectCode",value = "设备点检编码")
    private String equPointInspectionProjectCode;

    /**
     * 设备点检名称
     */
    @ApiModelProperty(name="equPointInspectionProjectName",value = "设备点检名称")
    private String equPointInspectionProjectName;

    /**
     * 单据状态(1-待点检 2-已点检)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待点检 2-已点检)")
    private Byte orderStatus;

    /**
     * 点检项目事项集合
     */
    @Transient
    @ApiModelProperty(name = "items",value = "点检项目事项集合")
    private List<EamEquPointInspectionProjectItem> items;
}
