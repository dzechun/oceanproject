package com.fantechs.provider.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lfz on 2020/12/10.
 */
@Data
public class PtlSortingDTO  implements Serializable {

    @ApiModelProperty(name="taskNo",value = "任务号")
    private String taskNo;

    @ApiModelProperty(name="customerNo",value = "相关单号")
    private String customerNo;

    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name="workerCode",value = "作业人员编码")
    private String workerCode;

    @ApiModelProperty(name="reviewNumber",value = "复核台编号")
    private String reviewNumber;

    @ApiModelProperty(name="isWhole",value = "是否整零（1-整 0-零）")
    private String isWhole;

    @ApiModelProperty(name="details",value = "任务明细")
    private List<PtlSortingDetailDTO> details;
}
