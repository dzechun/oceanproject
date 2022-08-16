package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsIpqcInspectionOrderDetSample extends BaseQuery implements Serializable {

    /**
     * IPQC检验单明细ID
     */
    @ApiModelProperty(name="inspectionOrderCode",value = "IPQC检验单明细ID")
    private Long ipqcInspectionOrderDetId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;
}
