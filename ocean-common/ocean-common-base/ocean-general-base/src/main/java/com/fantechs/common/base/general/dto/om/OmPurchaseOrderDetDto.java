package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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
}
