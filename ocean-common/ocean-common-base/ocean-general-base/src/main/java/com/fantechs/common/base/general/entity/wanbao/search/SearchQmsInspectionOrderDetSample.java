package com.fantechs.common.base.general.entity.wanbao.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
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

    /**
     * 厂内码
     */
    @ApiModelProperty(name="factoryBarcode",value = "厂内码")
    private String factoryBarcode;

}
