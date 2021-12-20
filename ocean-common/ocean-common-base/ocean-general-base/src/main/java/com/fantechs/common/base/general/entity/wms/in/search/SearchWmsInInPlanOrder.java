package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsInInPlanOrder extends BaseQuery implements Serializable {

    /**
     * 入库计划单编码
     */
    @ApiModelProperty(name="inPlanOrderCode",value = "入库计划单编码")
    private String inPlanOrderCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    private String sysOrderTypeCode;

    /**
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    private String sourceSysOrderTypeCode;

    /**
     * 单据状态(1-待作业 2-作业中 2-完成 3-待分配 4-分配中 5-已分配)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待作业 2-作业中 2-完成 3-待分配 4-分配中 5-已分配)")
    private Byte orderStatus;

    /**
     * 计划开始时间1
     */
    @ApiModelProperty(name="planStartTime1",value = "计划开始时间1")
    private String planStartTime1;
    /**
     * 计划开始时间2
     */
    @ApiModelProperty(name="planStartTime2",value = "计划开始时间2")
    private String planStartTime2;

    /**
     * 计划完成时间1
     */
    @ApiModelProperty(name="planEndTime1",value = "计划完成时间2")
    private String planEndTime1;

    /**
     * 计划完成时间2
     */
    @ApiModelProperty(name="planEndTime2",value = "计划完成时间2")
    private String planEndTime2;


}
