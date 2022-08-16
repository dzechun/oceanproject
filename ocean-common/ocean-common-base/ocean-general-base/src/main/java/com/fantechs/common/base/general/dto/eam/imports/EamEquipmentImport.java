package com.fantechs.common.base.general.dto.eam.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class EamEquipmentImport implements Serializable {

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
     * 设备类别(必填)
     */
    @Excel(name = "设备类别(必填)", height = 20, width = 30)
    @ApiModelProperty(name="equipmentCategoryCode" ,value="设备类别(必填)")
    private String equipmentCategoryCode;

    /**
     * 设备类别ID
     */
    @ApiModelProperty(name="equipmentCategoryId" ,value="设备类别ID")
    private Long equipmentCategoryId;

    /**
     * 设备管理员编码
     */
    @Excel(name = "设备管理员编码", height = 20, width = 30)
    @ApiModelProperty(name="userCode" ,value="设备管理员编码")
    private String userCode;

    /**
     * 设备管理员ID
     */
    @ApiModelProperty(name="equipmentMgtUserId" ,value="设备管理员ID")
    private Long equipmentMgtUserId;

    /**
     * 工厂编码
     */
    @Excel(name = "工厂编码", height = 20, width = 30)
    @ApiModelProperty(name="factoryCode" ,value="工厂编码")
    private String factoryCode;

    /**
     * 工厂ID
     */
    @ApiModelProperty(name="factoryId" ,value="工厂ID")
    private Long factoryId;

    /**
     * 车间编码
     */
    @Excel(name = "车间编码", height = 20, width = 30)
    @ApiModelProperty(name="workShopCode" ,value="车间编码")
    private String workShopCode;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId" ,value="车间ID")
    private Long workShopId;

    /**
     * 产线编码
     */
    @Excel(name = "产线编码", height = 20, width = 30)
    @ApiModelProperty(name="proLineCode" ,value="产线编码")
    private String proLineCode;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId" ,value="产线ID")
    private Long proLineId;

    /**
     * 最大使用次数
     */
    @Excel(name = "最大使用次数", height = 20, width = 30)
    @ApiModelProperty(name="maxUsageTime",value = "最大使用次数")
    private Integer maxUsageTime;

    /**
     * 警告次数
     */
    @ApiModelProperty(name="warningTime",value = "警告次数")
    @Excel(name = "警告次数", height = 20, width = 30)
    private Integer warningTime;

    /**
     * 最大使用天数
     */
    @ApiModelProperty(name="maxUsageDays",value = "最大使用天数")
    @Excel(name = "最大使用天数", height = 20, width = 30)
    private Integer maxUsageDays;

    /**
     * 警告天数
     */
    @ApiModelProperty(name="warningDays",value = "警告天数")
    @Excel(name = "警告天数", height = 20, width = 30)
    private Integer warningDays;

    /**
     * 长(cm)
     */
    @ApiModelProperty(name="length",value = "长(cm)")
    @Excel(name = "长(cm)", height = 20, width = 30)
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @ApiModelProperty(name="width",value = "宽(cm)")
    @Excel(name = "宽(cm)", height = 20, width = 30)
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @ApiModelProperty(name="height",value = "高(cm)")
    @Excel(name = "高(cm)", height = 20, width = 30)
    private BigDecimal height;

    /**
     * 体积(cm3)
     */
    @ApiModelProperty(name="volume",value = "体积(cm3)")
    @Excel(name = "体积(cm3)", height = 20, width = 30)
    private BigDecimal volume;

    /**
     * 重量(kg)
     */
    @ApiModelProperty(name="weight",value = "重量(kg)")
    @Excel(name = "重量(kg)", height = 20, width = 30)
    private BigDecimal weight;

    /**
     * 电源(kw)
     */
    @ApiModelProperty(name="power",value = "电源(kw)")
    @Excel(name = "电源(kw)", height = 20, width = 30)
    private BigDecimal power;

    /**
     * 保养最大使用次数
     */
    @ApiModelProperty(name="maintainMaxUsageTime",value = "保养最大使用次数")
    @Excel(name = "保养最大使用次数", height = 20, width = 30)
    private Integer maintainMaxUsageTime;

    /**
     * 保养警告次数
     */
    @ApiModelProperty(name="maintainWarningTime",value = "保养警告次数")
    @Excel(name = "保养警告次数", height = 20, width = 30)
    private Integer maintainWarningTime;

    /**
     * 保养周期(天)
     */
    @ApiModelProperty(name="maintainCycle",value = "保养周期(天)")
    @Excel(name = "保养周期(天)", height = 20, width = 30)
    private Integer maintainCycle;

    /**
     * 保养警告天数
     */
    @ApiModelProperty(name="maintainWarningDays",value = "保养警告天数")
    @Excel(name = "保养警告天数", height = 20, width = 30)
    private Integer maintainWarningDays;

    /**
     * 资产编码
     */
    @Excel(name = "资产编码",  height = 20, width = 30)
    @ApiModelProperty(name="assetCode" ,value="资产编码")
    private String assetCode;

    /**
     * 设备条码
     */
    @Excel(name = "设备条码", height = 20, width = 30)
    @ApiModelProperty(name="equipmentBarcode" ,value="治具条码")
    private String equipmentBarcode;

    /**
     * 已使用次数
     */
    @Excel(name = "已使用次数", height = 20, width = 30)
    @ApiModelProperty(name="currentUsageTime" ,value="已使用次数")
    private Integer currentUsageTime;

    /**
     * 已使用天数
     */
    @Excel(name = "已使用天数", height = 20, width = 30)
    @ApiModelProperty(name="currentUsageDays" ,value="已使用天数")
    private Integer currentUsageDays;

    /**
     * 备用件编码
     */
    @Excel(name = "备用件编码",  height = 20, width = 30)
    @ApiModelProperty(name="sparePartCode" ,value="备用件编码")
    private String sparePartCode;

    /**
     * 备用件ID
     */
    @ApiModelProperty(name="sparePartId" ,value="备用件ID")
    private Long sparePartId;
}
