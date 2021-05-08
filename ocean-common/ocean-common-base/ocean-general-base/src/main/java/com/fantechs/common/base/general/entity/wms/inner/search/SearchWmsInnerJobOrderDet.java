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

    private List<Byte> orderStatusList;

    private Long jobOrderDetId;
}
