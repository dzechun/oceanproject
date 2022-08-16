package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EamEquipmentDto extends EamEquipment implements Serializable {

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
     * 设备类别
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryDesc",value = "设备类别")
    @Excel(name = "设备类别", height = 20, width = 30,orderNum="5")
    private String equipmentCategoryDesc;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="8")
    private String warehouseName;

    /**
     * 车间名称
     */
    @Transient
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;


    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(name = "proLineName",value = "产线名称")
    private String proLineName;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name = "sectionName",value = "产线名称")
    private String sectionName;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    private String processName;

    /**
     * 工序编码
     */
    @Transient
    @ApiModelProperty(name = "processCode",value = "工序编码")
    private String processCode;

    /**
     * 工位名称
     */
    @Transient
    @ApiModelProperty(name = "stationName",value = "工位名称")
    private String stationName;

    /**
     * 工厂名称
     */
    @Transient
    @ApiModelProperty(name = "factoryName",value = "工厂名称")
    private String factoryName;

    /**
     * 保养编码
     */
    @ApiModelProperty(name="equipmenMaintainProjectCode",value = "保养编码")
    private String equipmenMaintainProjectCode;

    /**
     * 设备管理员用户名称
     */
    @Transient
    @ApiModelProperty(name = "equipmentMgtUserName",value = "设备管理员用户名称")
    private String equipmentMgtUserName;

}
