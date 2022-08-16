package com.fantechs.common.base.general.entity.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesPmWorkOrderBom extends BaseQuery implements Serializable {

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;

    /**
     * 零件物料ID
     */
    @ApiModelProperty(name="partMaterialId" ,value="物料ID")
    private Long partMaterialId;

    /**
     * 零件料号
     */
    @ApiModelProperty(name="partMaterialCode" ,value="零件料号")
    private String partMaterialCode;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 预留项目号(雷赛接口)
     */
    @ApiModelProperty(name="option1" ,value="预留项目号")
    private String option1;
}
