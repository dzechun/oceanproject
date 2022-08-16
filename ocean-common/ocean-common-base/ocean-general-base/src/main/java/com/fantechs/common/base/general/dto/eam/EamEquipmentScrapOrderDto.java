package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EamEquipmentScrapOrderDto extends EamEquipmentScrapOrder implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 申请部门
     */
    @Transient
    @ApiModelProperty(name = "applyDeptName",value = "申请部门")
    @Excel(name = "申请部门", height = 20, width = 30,orderNum="3")
    private String applyDeptName;

    /**
     * 使用人成本中心
     */
    @Transient
    @ApiModelProperty(name = "useDeptName",value = "使用人成本中心")
    @Excel(name = "使用人成本中心", height = 20, width = 30,orderNum="6")
    private String useDeptName;

    /**
     * 申请人姓名
     */
    @Transient
    @ApiModelProperty(name = "applyUserName",value = "申请人姓名")
    @Excel(name = "申请人姓名", height = 20, width = 30,orderNum="2")
    private String applyUserName;

    /**
     * 使用人姓名
     */
    @Transient
    @ApiModelProperty(name = "useUserName",value = "使用人姓名")
    @Excel(name = "使用人姓名", height = 20, width = 30,orderNum="5")
    private String useUserName;

    /**
     * 设备报废单明细
     */
    @ApiModelProperty(name = "list",value = "设备报废单明细")
    private List<EamEquipmentScrapOrderDetDto> list = new ArrayList<>();

}
