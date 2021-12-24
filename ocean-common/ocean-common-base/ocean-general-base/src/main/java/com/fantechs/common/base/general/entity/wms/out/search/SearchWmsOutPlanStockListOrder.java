package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchWmsOutPlanStockListOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name="planStockListOrderId",value = "备料计划单ID")
    private Long planStockListOrderId;

    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    private String coreSourceSysOrderTypeCode;

    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    private String sourceSysOrderTypeCode;

    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    private String sysOrderTypeCode;

    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    private Byte sourceBigType;

    @ApiModelProperty(name="planStockListOrderCode",value = "备料计划单编码")
    private String planStockListOrderCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 订单状态(1-待执行 2-执行中 3-已执行)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-待执行 2-执行中 3-已执行)")
    private Byte orderStatus;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;


}
