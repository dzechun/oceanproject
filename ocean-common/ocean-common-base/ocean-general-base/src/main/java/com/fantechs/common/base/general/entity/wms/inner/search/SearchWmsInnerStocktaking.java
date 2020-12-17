package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsInnerStocktaking extends BaseQuery implements Serializable {

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="stocktakingCode",value = "盘点单号")
    private String stocktakingCode;

    /**
     * 线别代码
     */
    @ApiModelProperty(name="proCode" ,value="线别代码")
    private String proCode;

    /**
     * 线别名称
     */
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;

    /**
     * 车间编码
     */
    @Excel(name = "车间编码", height = 20, width = 30,orderNum="1")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Excel(name = "车间名称", height = 20, width = 30,orderNum="2")
    private String workShopName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;
}
