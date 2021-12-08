package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsBadnessManageBarcode extends BaseQuery implements Serializable {
    /**
     * 不良品处理ID
     */
    @ApiModelProperty(name = "badnessManageId", value = "不良品处理ID")
    private Long badnessManageId;

}
