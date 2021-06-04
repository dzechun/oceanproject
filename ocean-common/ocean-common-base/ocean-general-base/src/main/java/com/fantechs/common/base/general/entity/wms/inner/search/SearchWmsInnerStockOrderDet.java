package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/28
 */
@Data
public class SearchWmsInnerStockOrderDet extends BaseQuery implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(name = "stockOrderId",value = "id")
    private Long stockOrderId;

    /**
     * 储位
     */
    @ApiModelProperty(name = "storageName",value = "储位")
    private String storageName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;
}
