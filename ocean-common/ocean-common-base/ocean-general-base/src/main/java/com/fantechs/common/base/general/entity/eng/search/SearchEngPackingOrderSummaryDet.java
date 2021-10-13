package com.fantechs.common.base.general.entity.eng.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchEngPackingOrderSummaryDet extends BaseQuery implements Serializable {

    /**
     * 装箱汇总ID
     */
    @ApiModelProperty(name="packingOrderSummaryId",value = "装箱汇总ID")
    private Long packingOrderSummaryId;

    /**
     *汇总明细状态(1-待收货 2-收货中 3-待上架 4-完成)
     * PDA收货只展示待收货及收货中，PDA创建上架单只展示待上架状态
     */
    @ApiModelProperty(name = "summaryDetStatus",value = "汇总明细状态(1-待收货 2-收货中 3-待上架 4-完成)" +
            "PDA收货只展示待收货及收货中，PDA创建上架单只展示待上架状态")
    private List<Byte> summaryDetStatus;

    /**
     * 原材料编码
     */
    @ApiModelProperty(name="rawMaterialCode",value = "原材料编码")
    private String rawMaterialCode;

    /**
     * 装箱清单id
     */
    @ApiModelProperty(name = "packingOrderId",value = "装箱清单id")
    private Long packingOrderId;

    /**
     * 材料编码
     */
    @ApiModelProperty(name = "materialCode",value = "材料编码")
    private String materialCode;

    /**
     * 货物名称
     */
    @ApiModelProperty(name = "materialName",value = "货物名称")
    private String materialName;

    /**
     * 规格
     */
    @ApiModelProperty(name = "spec",value = "规格")
    private String spec;
}
