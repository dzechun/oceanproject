package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsAndinStorageQuarantine extends BaseQuery implements Serializable {

    /**
     * 入库待检ID
     */
    @ApiModelProperty(name="andinStorageQuarantineId",value = "入库待检ID")
    private Long andinStorageQuarantineId;

    /**
     * 栈板ID
     */
    @ApiModelProperty(name="palletId",value = "栈板ID")
    private Long palletId;

    /**
     * 待检区域ID
     */
    @ApiModelProperty(name="inspectionWaitingAreaId",value = "待检区域ID")
    private Long inspectionWaitingAreaId;

    /**
     * 生产工单号
     */
    @ApiModelProperty(name = "workOrderCode",value = "生产工单号")
    private String workOrderCode;

    private static final long serialVersionUID = 1L;
}
