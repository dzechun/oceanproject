package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBook;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBookAttachment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBook;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBookAttachment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class EamHtEquipmentStandingBookDto extends EamHtEquipmentStandingBook implements Serializable {

    /**
     * 设备编码
     */
    @Transient
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    @Excel(name = "设备编码", height = 20, width = 30,orderNum="1")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @Transient
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="2")
    private String equipmentName;

    /**
     * 设备描述
     */
    @Transient
    @ApiModelProperty(name="equipmentDesc",value = "设备描述")
    @Excel(name = "设备描述", height = 20, width = 30,orderNum="3")
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @Transient
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="4")
    private String equipmentModel;

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
    @ApiModelProperty(name = "equipmentCategoryName",value = "设备类别")
    @Excel(name = "设备类别", height = 20, width = 30,orderNum="7")
    private String equipmentCategoryName;

    /**
     * 资产编码
     */
    @Transient
    @ApiModelProperty(name = "assetCode",value = "资产编码")
    @Excel(name = "资产编码", height = 20, width = 30,orderNum="1")
    private String assetCode;

    /**
     * 部门名称
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "部门名称")
    @Excel(name = "部门名称", height = 20, width = 30,orderNum="10")
    private String deptName;

    /**
     * 设备状态(1-空转运行 2-待料停机 3-上料停机 4-生产中 5-待生产 6-点检中 7-保养中 8-维修中 9-已报废)
     */
    @Transient
    @ApiModelProperty(name = "equipmentStatus",value = "设备状态(1-空转运行 2-待料停机 3-上料停机 4-生产中 5-待生产 6-点检中 7-保养中 8-维修中 9-已报废)")
    @Excel(name = "设备状态(1-空转运行 2-待料停机 3-上料停机 4-生产中 5-待生产 6-点检中 7-保养中 8-维修中 9-已报废)", height = 20, width = 30,orderNum="12")
    private Byte equipmentStatus;

    /**
     * 设备条码
     */
    @Transient
    @ApiModelProperty(name = "equipmentBarcode",value = "设备条码")
    @Excel(name = "设备条码", height = 20, width = 30,orderNum="2")
    private String equipmentBarcode;

    @Transient
    private List<EamHtEquipmentStandingBookAttachment> eamHtEquipmentStandingBookAttachments;



}
