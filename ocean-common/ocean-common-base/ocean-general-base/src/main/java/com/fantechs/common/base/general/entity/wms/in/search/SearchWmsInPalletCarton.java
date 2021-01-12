package com.fantechs.common.base.general.entity.wms.in.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SearchWmsInPalletCarton extends BaseQuery implements Serializable {

    /**
     * ID
     */
    @ApiModelProperty(name="palletCartonId",value = "ID")
    private Long palletCartonId;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    private String palletCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    private Long cartonCode;
}
