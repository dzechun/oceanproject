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
    @ApiModelProperty(name = "salesCode",value = "销售编码")
    private String  salesCode;

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
    @ApiModelProperty(name = "samePackageCode",value = "批次号/PO号")
    private String  samePackageCode;

    /**
     *  正品库存
     */
    @ApiModelProperty(name = "qty",value = "正品库存")
    private BigDecimal qty;


    /**
     *  国家
     */
    @ApiModelProperty(name = "countryName",value = "国家")
    private String  countryName;

    /**
     *  大区
     */
    @ApiModelProperty(name = "regionName",value = "大区")
    private String  regionName;

    /**
     *物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     *单位
     */
    @ApiModelProperty(name = "packingUnitName",value = "单位")
    private String  packingUnitName;

    /**
     *  产品描述
     */
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    private String  materialDesc;

    /**
     *  产品分类
     */
    @ApiModelProperty(name = "productCategory",value = "产品分类")
    private String  productCategory;

    /**
     *子库
     */
    @ApiModelProperty(name = "subWarehouseName",value = "子库")
    private String subWarehouseName;
}
