package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPtlJobOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    private Long jobOrderId;

    @ApiModelProperty(name="jobOrderCode",value = "任务单号")
    private String jobOrderCode;

    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;

    @ApiModelProperty(name="relatedOrderCode",value = "发货单号")
    private String despatchOrderCode;

    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(name="workerUserName",value = "作业人员名称")
    private String workerUserName;

    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待激活、2-已激活、3-完成、4-异常)")
    private Byte orderStatus;

    @ApiModelProperty(name="notOrderStatus",value = "不等于该单据状态(1-待激活、2-已激活、3-完成、4-异常)")
    private Byte notOrderStatus;

    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(name="warehouseAreaId",value = "仓库区域ID")
    private Long warehouseAreaId;
}
