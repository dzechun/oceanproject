package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @Auther: bingo.ren
 * @Date: 2021/1/20 19:03
 * @Description: 总计划打印工单相关信息
 * @Version: 1.0
 */
@Data
public class MasterPlanPrintWorkOrderDTO {
    @ApiModelProperty(name="workOrderCardId",value = "生成批号")
    @Excel(name = "生成批号", height = 20, width = 30,orderNum="3")
    private String workOrderCardId;
    @ApiModelProperty(name="productModelName",value = "型号")
    @Excel(name = "型号", height = 20, width = 30,orderNum="3")
    private String productModelName;
    @ApiModelProperty(name="materialQuality",value = "材质")
    @Excel(name = "材质", height = 20, width = 30,orderNum="3")
    private String materialQuality;
    @ApiModelProperty(name="color",value = "颜色")
    @Excel(name = "颜色", height = 20, width = 30,orderNum="3")
    private String color;
    @ApiModelProperty(name="workOrderQuantity",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="3")
    private BigDecimal workOrderQuantity;
    @ApiModelProperty(name="color",value = "排产日期")
    @Excel(name = "排产日期", height = 20, width = 30,orderNum="3")
    private Date scheduleDate;
    @ApiModelProperty(name="plannedEndTime",value = "完成日期")
    @Excel(name = "完成日期", height = 20, width = 30,orderNum="3")
    private Date plannedEndTime;
    @ApiModelProperty(name="mesPmProcessPlanList",value = "工序计划")
    @Excel(name = "工序计划", height = 20, width = 30,orderNum="3")
    private List<MesPmProcessPlan> mesPmProcessPlanList;
    @ApiModelProperty(name="color",value = "部件组成")
    @Excel(name = "部件组成", height = 20, width = 30,orderNum="3")
    private List<BasePlatePartsDetDto> basePlatePartsDetDtoList;
}
