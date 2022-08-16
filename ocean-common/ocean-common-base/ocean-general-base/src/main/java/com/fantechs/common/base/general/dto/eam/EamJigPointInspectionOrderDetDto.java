package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainOrderDet;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EamJigPointInspectionOrderDetDto extends EamJigPointInspectionOrderDet implements Serializable {

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
     * 点检事项名称
     */
    @Transient
    @ApiModelProperty(name = "jigPointInspectionProjectItemName",value = "点检事项名称")
    @Excel(name = "点检事项名称", height = 20, width = 30,orderNum="6")
    private String jigPointInspectionProjectItemName;

    /**
     * 点检事项描述
     */
    @Transient
    @ApiModelProperty(name = "jigPointInspectionProjectItemDesc",value = "点检事项描述")
    @Excel(name = "点检事项描述", height = 20, width = 30,orderNum="6")
    private String jigPointInspectionProjectItemDesc;

    /**
     * 判定类别(1-定性 2-定量)
     */
    @Transient
    @ApiModelProperty(name = "judgeType",value = "判定类别(1-定性 2-定量)")
    @Excel(name = "判定类别(1-定性 2-定量)", height = 20, width = 30,orderNum="6")
    private Byte judgeType;

    /**
     * 定量下限
     */
    @Transient
    @ApiModelProperty(name = "rationFloor",value = "定量下限")
    @Excel(name = "定量下限", height = 20, width = 30,orderNum="6")
    private BigDecimal rationFloor;

    /**
     * 定量上限
     */
    @Transient
    @ApiModelProperty(name = "rationUpperLimit",value = "定量上限")
    @Excel(name = "定量上限", height = 20, width = 30,orderNum="6")
    private BigDecimal rationUpperLimit;
}
