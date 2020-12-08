package com.fantechs.common.base.electronic.entity.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchSmtSortingList extends BaseQuery implements Serializable {

    /**
     * 分拣单Id
     */
    @ApiModelProperty(name="sortingListId",value = "分拣单Id")
    private Long sortingListId;

    /**
     * 分拣单号
     */
    @ApiModelProperty(name="sortingLisCode",value = "分拣单号")
    private String sortingLisCode;

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
