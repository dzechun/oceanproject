package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchStorageMonthEndInventory extends BaseQuery implements Serializable {

    /**
     * 仓库Id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库Id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName",value = "仓库区域名称")
    private String warehouseAreaName;

    /**
     * 储位名称
     */
    @ApiModelProperty(name="storageName",value = "储位名称")
    private String storageName;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 产品料号编码
     */
    @ApiModelProperty(name="materialDesc",value = "产品料号编码")
    private String materialDesc;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="materialName",value = "产品型号")
    private String materialName;

    /**
     * 月份
     */
    @ApiModelProperty(name="month",value = "月份")
    private String month;

    /**
     * 客户名称
     */
    @ApiModelProperty(name = "supplierName", value = "客户名称")
    private String supplierName;

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractCode", value = "合同号")
    private String contractCode;

}
