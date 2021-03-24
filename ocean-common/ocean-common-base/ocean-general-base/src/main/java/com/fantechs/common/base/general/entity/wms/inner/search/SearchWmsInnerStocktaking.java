package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsInnerStocktaking extends BaseQuery implements Serializable {

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="stocktakingCode",value = "盘点单号")
    @Excel(name = "盘点单号", height = 20, width = 30,orderNum="")
    private String stocktakingCode;

    /**
     * 盘点员Id
     */
    @ApiModelProperty(name="stockistId",value = "盘点员Id")
    @Excel(name = "盘点员Id", height = 20, width = 30,orderNum="")
    private Long stockistId;

    /**
     * 盘点状态(0、待盘点 1、盘点中 2、盘点完成)
     */
    @ApiModelProperty(name="status",value = "盘点状态(0、待盘点 1、盘点中 2、盘点完成)")
    @Excel(name = "盘点状态(0、待盘点 1、盘点中 2、盘点完成)", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 盘点方式(0、月盘 1、抽盘)
     */
    @ApiModelProperty(name="stocktakingMode",value = "盘点方式(0、月盘 1、抽盘)")
    @Excel(name = "盘点方式(0、月盘 1、抽盘)", height = 20, width = 30,orderNum="")
    private Byte stocktakingMode;

    /**
     * 状态查询标记(传0则查询出待盘点和盘点中的单据)
     */
    @ApiModelProperty(name = "queryMark",value = "状态查询标记(传0则查询出待盘点和盘点中的单据)")
    private Integer queryMark;
}
