package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsInnerInventoryScrap extends BaseQuery implements Serializable {

    /**
     * 盘存转报废单号
     */
    @ApiModelProperty(name="inventoryScrapCode",value = "盘存转报废单号")
    private String inventoryScrapCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserName",value = "处理人")
    private String processorUserName;

    /**
     * 修改人(操作人)
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人(操作人)")
    private String modifiedUserName;

    /**
     * 单据状态（0-待报废 1-报废中 2-报废完成）
     */
    @ApiModelProperty(name="inventoryScrapStatus",value = "单据状态（0-待报废 1-报废中 2-报废完成）")
    private Byte inventoryScrapStatus;

}
