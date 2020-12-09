package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtSorting extends BaseQuery implements Serializable {

    /**
     * 分拣单Id
     */
    @ApiModelProperty(name="sortingId",value = "分拣单Id")
    private Long sortingId;

    /**
     * 分拣单号
     */
    @ApiModelProperty(name="sortingCode",value = "分拣单号")
    private String sortingCode;

    /**
     * 工单号(预留)
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号(预留)")
    private String workOrderCode;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode",value = "储位编码")
    private String storageCode;
}
