package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Data
public class SearchInventoryAgeUreport extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "warehouseId",value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(name = "warehouseAreaId",value = "库区id")
    private Long warehouseAreaId;

    @ApiModelProperty(name = "storageId",value = "库位id")
    private Long storageId;

    @ApiModelProperty(name = "materialId",value = "物料")
    private Long materialId;

    @ApiModelProperty(name = "orgId",value = "组织")
    private Long orgId;

    /**
     *  销售编码
     */
    @ApiModelProperty(name = "salesOrderCode",value = "销售编码")
    private String  salesOrderCode;

    /**
     *  业务员
     */
    @ApiModelProperty(name = "salesUserName",value = "业务员")
    private String  salesUserName;

    /**
     *  客户
     */
    @ApiModelProperty(name = "supplierName",value = "客户")
    private String  supplierName;

    /**
     *  物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String  materialCode;

    /**
     *  库区
     */
    @ApiModelProperty(name = "storageCode",value = "库区")
    private String  storageCode;

    /**
     *  型号
     */
    @ApiModelProperty(name = "productModelName",value = "型号")
    private String  productModelName;

    /**
     *  批次号/PO号
     */
    @ApiModelProperty(name = "poCode",value = "批次号/PO号")
    private String  poCode;

    /**
     *  正品库存
     */
    @ApiModelProperty(name = "qty",value = "正品库存")
    private BigDecimal qty;

}
