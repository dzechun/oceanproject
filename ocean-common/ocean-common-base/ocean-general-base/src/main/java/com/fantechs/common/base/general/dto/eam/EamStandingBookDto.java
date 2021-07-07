package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamStandingBook;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class EamStandingBookDto extends EamStandingBook implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="14")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="16")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 设备编码
     */
    @Transient
    @ApiModelProperty(name = "equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="2")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @Transient
    @ApiModelProperty(name = "equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="3")
    private String equipmentName;

    /**
     * 设备描述
     */
    @Transient
    @ApiModelProperty(name = "equipmentDesc",value = "设备描述")
    @Excel(name = "设备描述", height = 20, width = 30,orderNum="4")
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @Transient
    @ApiModelProperty(name = "equipmentModel",value = "设备型号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="5")
    private String equipmentModel;

    /**
     * 设备类别
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryDesc",value = "设备类别")
    @Excel(name = "设备类别", height = 20, width = 30,orderNum="6")
    private String equipmentCategoryDesc;

    /**
     * 功率(kw)
     */
    @Transient
    @ApiModelProperty(name = "power",value = "功率(kw)")
    @Excel(name = "功率(kw)", height = 20, width = 30,orderNum="8")
    private BigDecimal power;

    /**
     * 重量(kg)
     */
    @Transient
    @ApiModelProperty(name = "weight",value = "重量(kg)")
    @Excel(name = "重量(kg)", height = 20, width = 30,orderNum="9")
    private BigDecimal weight;

    /**
     * 出厂日期
     */
    @Transient
    @ApiModelProperty(name = "releaseDate",value = "出厂日期")
    @Excel(name = "出厂日期", height = 20, width = 30,orderNum="10")
    private Date releaseDate;

    /**
     * 部门名称
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "部门名称")
    @Excel(name = "部门名称", height = 20, width = 30,orderNum="11")
    private String deptName;

    /**
     * 使用状态(1-使用中 2-空闲)
     */
    @Transient
    @ApiModelProperty(name = "usageStatus",value = "使用状态(1-使用中 2-空闲)")
    @Excel(name = "使用状态(1-使用中 2-空闲)", height = 20, width = 30,orderNum="12")
    private Byte usageStatus;

}
