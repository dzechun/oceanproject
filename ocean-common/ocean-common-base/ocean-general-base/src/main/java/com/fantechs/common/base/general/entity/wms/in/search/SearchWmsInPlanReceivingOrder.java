package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/13
 */
@Data
public class SearchWmsInPlanReceivingOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "planReceivingOrderId",value = "计划单id")
    private Long planReceivingOrderId;

    @ApiModelProperty(name = "coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    private String sourceSysOrderTypeCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    private String sysOrderTypeCode;

    /**
     * 收货计划单号
     */
    @ApiModelProperty(name="planReceivingOrderCode",value = "收货计划单号")
    private String planReceivingOrderCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 单据状态(1-待执行 2-执行中 3-收货完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待执行 2-执行中 3-收货完成)")
    private Byte orderStatus;
}
