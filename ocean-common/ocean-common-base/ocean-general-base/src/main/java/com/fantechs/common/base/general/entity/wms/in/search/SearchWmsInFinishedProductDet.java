package com.fantechs.common.base.general.entity.wms.in.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsInFinishedProductDet extends BaseQuery implements Serializable {

    /**
     * 成品入库单ID
     */
    @ApiModelProperty(name="finishedProductId",value = "成品入库单ID")
    private String finishedProductId;

}