package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerJobOrderDetDto extends WmsInnerJobOrderDet implements Serializable {
    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 移出库位
     */
    @Transient
    @ApiModelProperty(name = "outStorageName",value = "移出库位")
    private String outStorageName;

    /**
     * 移出库位编码
     */
    @Transient
    @ApiModelProperty(name = "outStorageCode",value = "移出库位编码")
    private String outStorageCode;

    /**
     * 移入库位
     */
    @Transient
    @ApiModelProperty(name = "inStorageName",value = "移入库位")
    private String inStorageName;

    /**
     * 移入库位编码
     */
    @Transient
    @ApiModelProperty(name = "inStorageCode",value = "移入库位编码")
    private String inStorageCode;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    /**
     * 库存状态
     */
    @Transient
    @ApiModelProperty(name = "inventoryStatusName",value = "库存状态")
    private String inventoryStatusName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 月台名称
     */
    @Transient
    @ApiModelProperty(name = "platformName",value = "月台")
    private String platformName;

    /**
     * proName
     */
    @ApiModelProperty(name = "proName",value = "产线")
    private String proName;

    @Transient
    @ApiModelProperty(name = "releaseName",value = "堆垛释放人")
    private String releaseName;

    @Transient
    @ApiModelProperty(name = "salesCode",value = "销售编码")
    private String salesCode;

    @Transient
    @ApiModelProperty(name = "samePackageCode",value = "PO")
    private String samePackageCode;
}
