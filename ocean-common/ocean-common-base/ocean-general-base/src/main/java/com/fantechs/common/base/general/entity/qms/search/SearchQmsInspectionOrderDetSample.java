package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsInspectionOrderDetSample extends BaseQuery implements Serializable {

    /**
     * 检验单明细ID
     */
    @ApiModelProperty(name="inspectionOrderDetId",value = "检验单明细ID")
    private Long inspectionOrderDetId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

}
