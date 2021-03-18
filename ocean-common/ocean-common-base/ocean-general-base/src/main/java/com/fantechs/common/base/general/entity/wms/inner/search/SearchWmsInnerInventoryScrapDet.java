package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchWmsInnerInventoryScrapDet extends BaseQuery implements Serializable {

    /**
     * 盘存转报废单ID
     */
    @ApiModelProperty(name="inventoryScrapId",value = "盘存转报废单ID")
    private Long inventoryScrapId;


}
