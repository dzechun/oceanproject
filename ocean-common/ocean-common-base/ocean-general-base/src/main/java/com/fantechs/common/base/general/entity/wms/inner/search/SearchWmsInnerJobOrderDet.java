package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchWmsInnerJobOrderDet extends BaseQuery implements Serializable {
    /**
     * 上架单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "上架单ID")
    private Long jobOrderId;

    private List<Byte> lineStatusList;

    private Long jobOrderDetId;

    @ApiModelProperty(name="nonShiftStorageStatus",value = "不等于，移位状态(1-待作业 2-拣货中 3-上架中 4-已完成)")
    private byte nonShiftStorageStatus;

    @ApiModelProperty(name="ifFiltrate",value = "是否筛选（0，否  1，是）")
    private byte ifFiltrate;

}
