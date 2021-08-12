package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class EamEquipmentDataGroupDto extends EamEquipmentDataGroup implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="16")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="18")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 设备总计
     */
    @Transient
    @ApiModelProperty(name = "equipmentTotal",value = "设备总计")
    private BigDecimal equipmentTotal;

    /**
     * 在线设备
     */
    @Transient
    @ApiModelProperty(name = "equipmentOnline",value = "在线设备")
    private BigDecimal equipmentOnline;

    /**
     * 离线设备
     */
    @Transient
    @ApiModelProperty(name = "equipmentOffline",value = "离线设备")
    private BigDecimal equipmentOffline;

    /**
     * 异常设备
     */
    @Transient
    @ApiModelProperty(name = "equipmentUnknown",value = "异常设备")
    private BigDecimal equipmentUnknown;

    /**
     * 分组参数
     */
    @Transient
    List<EamEquipmentDataGroupParamDto> eamEquipmentDataGroupParamDtos;

}
