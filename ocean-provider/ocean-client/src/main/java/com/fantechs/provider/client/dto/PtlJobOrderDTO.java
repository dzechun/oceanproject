package com.fantechs.provider.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lfz on 2020/12/10.
 */
@Data
public class PtlJobOrderDTO implements Serializable {

    @ApiModelProperty(name="taskNo",value = "任务号")
    private String taskNo;

    @ApiModelProperty(name="customerNo",value = "拣货单号")
    private String customerNo;

    @ApiModelProperty(name="deliveryNo",value = "发货单号")
    private String deliveryNo;

    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name="workerCode",value = "作业人员编码")
    private String workerCode;

    @ApiModelProperty(name="reviewNumber",value = "复核台编号")
    private String reviewNumber;

    @ApiModelProperty(name="details",value = "任务明细")
    private List<PtlJobOrderDetDTO> details;

    @ApiModelProperty(name="status",value = "状态(F-(复核)完成 C-取消)")
    private String status;
}
