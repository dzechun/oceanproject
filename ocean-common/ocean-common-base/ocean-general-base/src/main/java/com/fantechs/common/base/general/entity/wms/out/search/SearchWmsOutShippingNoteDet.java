package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchWmsOutShippingNoteDet extends BaseQuery implements Serializable {

    /**
     * 出货通知单ID
     */
    @ApiModelProperty(name="shippingNoteId",value = "出货通知单ID")
    private Long shippingNoteId;
}
