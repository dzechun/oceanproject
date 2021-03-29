package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SearchWmsInnerStocktakingDet extends BaseQuery implements Serializable {

    /**
     * 盘点单id
     */
    @ApiModelProperty(name="stocktakingId",value = "盘点单id")
    private Long stocktakingId;

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="stocktakingCode",value = "盘点单号")
    private String stocktakingCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    private String contractCode;

    /**
     * 栈板码
     */
    @ApiModelProperty(name="palletCode",value = "栈板码")
    private String palletCode;

    /**
     * 盘点结果(0、正常 1、盘盈 2、盘亏)
     */
    @ApiModelProperty(name="status",value = "盘点结果(0、正常 1、盘盈 2、盘亏)")
    private Byte inventoryResults;

    /**
     * 盘点员Id
     */
    @ApiModelProperty(name="stockistId",value = "盘点员Id")
    private Long stockistId;

    /**
     * 盘点员名称
     */
    @ApiModelProperty(name="stockistName",value = "盘点员名称")
    private String stockistName;
}
