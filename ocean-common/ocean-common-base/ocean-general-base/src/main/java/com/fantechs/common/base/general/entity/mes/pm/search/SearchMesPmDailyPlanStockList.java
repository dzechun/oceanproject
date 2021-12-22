package com.fantechs.common.base.general.entity.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesPmDailyPlanStockList extends BaseQuery implements Serializable {

    /**
     * 日计划备料表ID
     */
    @ApiModelProperty(name="dailyPlanStockListId",value = "日计划备料表ID")
    private Long dailyPlanStockListId;

    /**
     * 日计划明细表ID
     */
    @ApiModelProperty(name="dailyPlanDetId",value = "日计划明细表ID")
    private Long dailyPlanDetId;

    /**
     * 日计划ID
     */
    @ApiModelProperty(name="dailyPlanId",value = "日计划ID")
    private Long dailyPlanId;

    /**
     * 零件物料ID
     */
    @ApiModelProperty(name="partMaterialId",value = "零件物料ID")
    private Long partMaterialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    /**
     * 单个用量
     */
    @ApiModelProperty(name="singleQty",value = "单个用量")
    private BigDecimal singleQty;

    /**
     * 日计划使用数量
     */
    @ApiModelProperty(name="dailyPlanUsageQty",value = "日计划使用数量")
    private BigDecimal dailyPlanUsageQty;

    /**
     * 累计下发数量
     */
    @ApiModelProperty(name="totalIssueQty",value = "累计下发数量")
    private BigDecimal totalIssueQty;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    private Byte ifAllIssued;

    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

}
