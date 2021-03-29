package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktakingDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerStocktakingDetDto extends WmsInnerStocktakingDet implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    @Transient
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc",value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    @Transient
    private String materialDesc;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30)
    @Transient
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name="storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30)
    @Transient
    private String storageName;

    /**
     * 仓库区域Id
     */
    @ApiModelProperty(name="warehouseAreaId",value = "仓库区域Id")
    @Excel(name = "仓库区域Id", height = 20, width = 30,orderNum="")
    @Transient
    private Long warehouseAreaId;

    /**
     * 仓库区域编码
     */
    @ApiModelProperty(name="warehouseAreaCode",value = "仓库区域编码")
    @Excel(name = "仓库区域编码", height = 20, width = 30)
    @Transient
    private String warehouseAreaCode;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName",value = "仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30)
    @Transient
    private String warehouseAreaName;

    /**
     * 仓库Id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库Id")
    @Excel(name = "仓库Id", height = 20, width = 30,orderNum="")
    @Transient
    private Long warehouseId;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30)
    @Transient
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30)
    @Transient
    private String warehouseName;

    /**
     * 产品型号编码
     */
    @ApiModelProperty(name="productModelCode",value = "产品型号编码")
    @Excel(name = "产品型号编码", height = 20, width = 30)
    @Transient
    private String productModelCode;

    /**
     * 产品型号名称
     */
    @ApiModelProperty(name="productModelName",value = "产品型号名称")
    @Excel(name = "产品型号名称", height = 20, width = 30)
    @Transient
    private String productModelName;

    /**
     * 组织代码
     */
    @ApiModelProperty(name="organizationCode",value = "组织代码")
    @Excel(name = "组织代码", height = 20, width = 30)
    @Transient
    private String organizationCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    @Transient
    private String organizationName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30)
    @Transient
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30)
    @Transient
    private String modifiedUserName;

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="stocktakingCode",value = "盘点单号")
    @Excel(name = "盘点单号", height = 20, width = 30)
    @Transient
    private String stocktakingCode;

    /**
     * 盘点员
     */
    @ApiModelProperty(name="stockistName",value = "盘点员")
    @Excel(name = "盘点员", height = 20, width = 30)
    @Transient
    private String stockistName;
}
