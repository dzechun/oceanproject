package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchEngPackingOrderSummary extends BaseQuery implements Serializable {

    /**
     * 装箱单ID
     */
    @ApiModelProperty(name="packingOrderId",value = "装箱单ID")
    private Long packingOrderId;


    /**
     * 汇总状态(1-待收货 2-收货中 3-待上架 4-完成)
     * PDA收货只展示待收货及收货中状态 PDA上架只展示待上架状态
     */
    @ApiModelProperty(name = "summaryStatus",value = "汇总状态(1-待收货 2-收货中 3-待上架 4-完成)" +
            "PDA收货只展示待收货及收货中状态 PDA上架只展示待上架状态")
    private List<Byte> summaryStatus;
}
