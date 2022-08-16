package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class SearchBaseWarehousePersonnel extends BaseQuery {

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    private Long warehouseId;
}
