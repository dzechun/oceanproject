package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OmPurchaseOrderDetDto extends OmPurchaseOrderDet implements Serializable {

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(name = "warehouseId",value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     *  产品型号名称
     */
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    private String productModelName;

    /**
     * 物料类别名称
     */
    @ApiModelProperty(name="materialCategoryName",value = "物料类别名称")
    private String materialCategoryName;

    /**
     * 物料来源(0.自制件 1.虚拟件 2.采购件)
     */
    @ApiModelProperty(name="materialSource" ,value="物料来源(0.自制件 1.虚拟件 2.采购件)")
    private Integer materialSource;


    @ApiModelProperty(name="modifiedUserName",value = "修改人员名称")
    private String modifiedUserName;

    /**
     * 创建名称
     */
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    private String createUserName;

    /**
     * 组织
     */
    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;

    /**
     * 采购订单编码
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单编码")
    private String purchaseOrderCode;

    /**
     * 体积
     */
    @ApiModelProperty(name="volume",value = "体积")
    private BigDecimal volume;

    /**
     * 净重
     */
    @ApiModelProperty(name="netWeight",value = "净重")
    private BigDecimal netWeight;

    /**
     * 毛重
     */
    @ApiModelProperty(name="grossWeight",value = "毛重")
    private BigDecimal grossWeight;
}
