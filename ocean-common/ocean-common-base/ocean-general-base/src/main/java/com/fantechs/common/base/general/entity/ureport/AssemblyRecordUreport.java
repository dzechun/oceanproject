package com.fantechs.common.base.general.entity.ureport;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AssemblyRecordUreport extends ValidGroup implements Serializable {

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode",value = "工序编码")
    @Excel(name = "工序编码", height = 20, width = 30,orderNum = "1")
    private String processCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum = "2")
    private String processName;

    /**
     * 物料类别(1-物料 2-条码)
     */
    @ApiModelProperty(name="scanType",value = "物料类别(1-物料 2-条码)")
    @Excel(name = "物料类别", height = 20, width = 30,orderNum = "3",replace = {"物料_1","条码_2"})
    private Byte scanType;

    /**
     * 零件料号
     */
    @ApiModelProperty(name="materialCode",value = "零件料号")
    @Excel(name = "零件料号", height = 20, width = 30,orderNum = "4")
    private String materialCode;

    /**
     * 零件料号版本
     */
    @ApiModelProperty(name="materialVer",value = "零件料号版本")
    @Excel(name = "零件料号版本", height = 20, width = 30,orderNum = "5")
    private String materialVer;

    /**
     * 零件料号描述
     */
    @ApiModelProperty(name="materialDesc",value = "零件料号描述")
    @Excel(name = "零件料号描述", height = 20, width = 30,orderNum = "6")
    private String materialDesc;

    /**
     * 条码名称
     */
    @ApiModelProperty(name="labelCategoryName",value = "条码名称")
    @Excel(name = "条码名称", height = 20, width = 30,orderNum = "7")
    private String labelCategoryName;

    /**
     * 用量
     */
    @ApiModelProperty(name="dosage",value = "用量")
    @Excel(name = "用量", height = 20, width = 30,orderNum = "8")
    private BigDecimal dosage;

}
