package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamEquipmentMaintainOrderDto extends EamEquipmentMaintainOrder implements Serializable {

    /**
     * 设备编码
     */
    @Transient
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="3")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @Transient
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="4")
    private String equipmentName;

    /**
     * 设备类别名称
     */
    @Transient
    @ApiModelProperty(name="equipmentCategoryName",value = "设备类别名称")
    @Excel(name = "设备类别名称", height = 20, width = 30,orderNum="5")
    private String equipmentCategoryName;

    /**
     * 设备条码
     */
    @Transient
    @ApiModelProperty(name="equipmentBarcode",value = "设备条码")
    @Excel(name = "设备条码", height = 20, width = 30,orderNum="2")
    private String equipmentBarcode;

    /**
     * 设备保养项目编码
     */
    @Transient
    @ApiModelProperty(name="equipmentMaintainProjectCode",value = "设备保养项目编码")
    @Excel(name = "设备保养项目编码", height = 20, width = 30,orderNum="6")
    private String equipmentMaintainProjectCode;

    /**
     * 设备保养项目名称
     */
    @Transient
    @ApiModelProperty(name="equipmentMaintainProjectName",value = "设备保养项目名称")
    @Excel(name = "设备保养项目名称", height = 20, width = 30,orderNum="7")
    private String equipmentMaintainProjectName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
