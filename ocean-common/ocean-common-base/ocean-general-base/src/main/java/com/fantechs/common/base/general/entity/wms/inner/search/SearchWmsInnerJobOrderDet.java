package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchWmsInnerJobOrderDet extends BaseQuery implements Serializable {
    /**
     * 上架单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "上架单ID")
    private Long jobOrderId;

    private List<Byte> orderStatusList;

    private Long jobOrderDetId;

    @ApiModelProperty(name="nonShiftStorageStatus",value = "不等于，移位状态(1-待作业 2-拣货中 3-上架中 4-已完成)")
    private byte nonShiftStorageStatus;

    @ApiModelProperty(name = "jobOrderType",value = "作业类型(1-加工拣货 2-移位 3-上架 4-拣货 5-补货)")
    private byte jobOrderType;

    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;
}
