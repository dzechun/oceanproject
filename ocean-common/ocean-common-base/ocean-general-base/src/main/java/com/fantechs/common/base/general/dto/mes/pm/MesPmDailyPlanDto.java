package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class MesPmDailyPlanDto extends MesPmDailyPlan implements Serializable {

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "线别名称", height = 20, width = 30,orderNum = "3")
    private String proName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    @Excel(name = "创建人", height = 20, width = 30,orderNum = "6")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum = "8")
    private String modifiedUserName;

    /**
     * 生产日计划明细
     */

    @Transient
    List<MesPmDailyPlanDetDto> mesPmDailyPlanDetDtos;

}
