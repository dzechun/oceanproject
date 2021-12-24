package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchWmsOutPlanStockListOrderDet extends BaseQuery implements Serializable {

    /**
     * 日计划明细ID
     */
    @ApiModelProperty(name="planStockListOrderDetId",value = "备料计划单明细ID")
    private Long planStockListOrderDetId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单据编码")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据编码")
    private String sourceOrderCode;

    /**
     * 核心来源ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心来源ID")
    private Long coreSourceId;

    /**
     * 来源ID
     */
    @ApiModelProperty(name="sourceId",value = "来源ID")
    private Long sourceId;

    /**
     * 备料计划单ID
     */
    @ApiModelProperty(name="planStockListOrderId",value = "备料计划单ID")
    private Long planStockListOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 实际拣货数量
     */
    @ApiModelProperty(name="actualQty",value = "实际拣货数量")
    private BigDecimal actualQty;

    /**
     * 行状态(1-待出库 2-出库中 3-出库完成)
     */
    @ApiModelProperty(name="lineStatus",value = "行状态(1-待出库 2-出库中 3-出库完成)")
    private Byte lineStatus;

    /**
     * 出库用户ID
     */
    @ApiModelProperty(name="deliveryUserId",value = "出库用户ID")
    private Long deliveryUserId;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    private Byte ifAllIssued;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;


}
