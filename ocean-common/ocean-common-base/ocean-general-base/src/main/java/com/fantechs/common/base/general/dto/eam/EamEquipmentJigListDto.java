package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJigList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamEquipmentJigListDto extends EamEquipmentJigList implements Serializable {

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

    /**
     * 治具编码
     */
    @ApiModelProperty(name="jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="5")
    @Transient
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="6")
    @Transient
    private String jigName;

    /**
     * 治具描述
     */
    @ApiModelProperty(name="jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="7")
    @Transient
    private String jigDesc;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="8")
    @Transient
    private String jigModel;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Transient
    private Long equipmentId;

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="1")
    @Transient
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="2")
    @Transient
    private String equipmentName;

    /**
     * 设备描述
     */
    @ApiModelProperty(name="equipmentDesc",value = "设备描述")
    @Excel(name = "设备描述", height = 20, width = 30,orderNum="3")
    @Transient
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="4")
    @Transient
    private String equipmentModel;

}
