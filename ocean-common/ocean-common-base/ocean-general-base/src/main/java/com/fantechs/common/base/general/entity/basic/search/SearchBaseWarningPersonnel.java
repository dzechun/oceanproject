package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class SearchBaseWarningPersonnel extends BaseQuery {

    /**
     * 预警ID
     */
    @ApiModelProperty(name="warningId",value = "预警ID")
    private Long warningId;
}
