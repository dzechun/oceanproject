package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrder;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamEquipmentScrapOrderDetDto extends EamEquipmentScrapOrderDet implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 设备条码
     */
    @Transient
    @ApiModelProperty(name = "equipmentBarcode",value = "设备条码")
    @Excel(name = "设备条码", height = 20, width = 30,orderNum="6")
    private String equipmentBarcode;

    /**
     * 资产条码
     */
    @Transient
    @ApiModelProperty(name = "assetCode",value = "资产条码")
    @Excel(name = "资产条码", height = 20, width = 30,orderNum="6")
    private String assetCode;

    /**
     * 设备编码
     */
    @Transient
    @ApiModelProperty(name = "equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="6")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @Transient
    @ApiModelProperty(name = "equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="6")
    private String equipmentName;

    /**
     * 设备描述
     */
    @Transient
    @ApiModelProperty(name = "equipmentDesc",value = "设备描述")
    @Excel(name = "设备描述", height = 20, width = 30,orderNum="6")
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @Transient
    @ApiModelProperty(name = "equipmentModel",value = "设备型号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="6")
    private String equipmentModel;

    /**
     * 财产编码类别(1-固定资产  2-列管品)
     */
    @Transient
    @ApiModelProperty(name = "propertyCodeCategory",value = "财产编码类别(1-固定资产  2-列管品)")
    @Excel(name = "财产编码类别(1-固定资产  2-列管品)", height = 20, width = 30,orderNum="6")
    private Byte propertyCodeCategory;

    /**
     * 设备类别
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryName",value = "设备类别")
    @Excel(name = "设备类别", height = 20, width = 30,orderNum="6")
    private String equipmentCategoryName;

}
