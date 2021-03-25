package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesProcessReportWork;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesProcessReportWorkDto extends MesProcessReportWork implements Serializable {

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="")
    private String materialCode;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="")
    private String productModelName;

    /**
     * 主单位
     */
    @Transient
    @ApiModelProperty(name="mainUnit",value = "主单位")
    @Excel(name = "主单位", height = 20, width = 30,orderNum="")
    private String mainUnit;

    /**
     * 工序代码
     */
    @Transient
    @ApiModelProperty(name="processCode" ,value="工序代码")
    @Excel(name = "工序代码", height = 20, width = 30)
    private String processCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30)
    private String processName;

    /**
     * 班组代码
     */
    @Transient
    @ApiModelProperty(name="teamCode",value = "班组代码")
    @Excel(name = "班组代码", height = 20, width = 30)
    private String teamCode;

    /**
     * 班组名称
     */
    @Transient
    @ApiModelProperty(name="teamName",value = "班组名称")
    @Excel(name = "班组名称", height = 20, width = 30)
    private String teamName;

    /**
     * 员工编码
     */
    @Transient
    @ApiModelProperty(name="staffCode",value = "员工编码")
    @Excel(name = "员工编码", height = 20, width = 30)
    private String staffCode;

    /**
     * 员工名称
     */
    @Transient
    @ApiModelProperty(name="staffName",value = "员工名称")
    @Excel(name = "员工名称", height = 20, width = 30)
    private String staffName;

    /**
     * 报工总数
     */
    @Transient
    @ApiModelProperty(name="totalQuantity",value = "报工总数")
    @Excel(name = "报工总数", height = 20, width = 30,orderNum="")
    private BigDecimal totalQuantity;

}
