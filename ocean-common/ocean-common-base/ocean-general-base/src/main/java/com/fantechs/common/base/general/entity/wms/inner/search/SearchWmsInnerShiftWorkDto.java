package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsInnerShiftWorkDto extends BaseQuery implements Serializable {
    /**
     * 作业单号
     */
    @ApiModelProperty(name="jobOrderCode",value = "作业单号")
    private String jobOrderCode;

    /**
     * 货主
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主")
    private String materialOwnerName;

    /**
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 工作人员
     */
    @ApiModelProperty(name="workerName",value = "工作人员")
    private String workerName;

    /**
     * 单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成 6-待激活)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成 6-待激活)")
    private Byte orderStatus;
}
