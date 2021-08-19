package com.fantechs.common.base.general.dto.daq;

import com.fantechs.common.base.general.entity.daq.DaqEquipmentReEs;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class DaqEquipmentReEsDto extends DaqEquipmentReEs implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    private String modifiedUserName;


    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Transient
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Transient
    private String equipmentName;

    /**
     * 设备描述
     */
    @ApiModelProperty(name="equipmentDesc",value = "设备描述")
    @Transient
    private String equipmentDesc;


    /**
     * 设备型号
     */
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    @Transient
    private String equipmentModel;

    /**
     * 设备分组ID
     */
    @ApiModelProperty(name="equipmentDataGroupTd",value = "设备分组ID")
    @Transient
    private Long equipmentDataGroupTd;

    /**
     * 设备IP
     */
    @ApiModelProperty(name="equipmentIp",value = "设备IP")
    @Transient
    private String equipmentIp;

    /**
     * MAC地址
     */
    @ApiModelProperty(name="equipmentMacAddress",value = "MAC地址")
    @Transient
    private String equipmentMacAddress;
}
