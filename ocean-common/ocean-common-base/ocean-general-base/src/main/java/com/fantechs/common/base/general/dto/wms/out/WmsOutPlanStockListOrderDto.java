package com.fantechs.common.base.general.dto.wms.out;

import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class WmsOutPlanStockListOrderDto extends WmsOutPlanStockListOrder implements Serializable {

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

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
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 备料计划明细
     */

    @Transient
    List<WmsOutPlanStockListOrderDetDto> wmsOutPlanStockListOrderDetDtos;

}
