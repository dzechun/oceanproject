package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsOutDeliveryOrderDetDto extends WmsOutDeliveryOrderDet implements Serializable {

    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料规格")
    @Excel(name = "物料规格", height = 20, width = 30)
    private String materialDesc;

    @Transient
    @ApiModelProperty(name="storageCode" ,value="库位编码")
    @Excel(name = "库位编码", height = 20, width = 30)
    private String storageCode;

    @Transient
    @ApiModelProperty(name="pickingStorageCode" ,value="拣货库位编码")
    @Excel(name = "拣货库位编码", height = 20, width = 30)
    private String pickingStorageCode;

    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30)
    private String warehouseName;

    @Transient
    @ApiModelProperty(name="volume" ,value="体积")
    @Excel(name = "体积", height = 20, width = 30)
    private BigDecimal volume;

    @Transient
    @ApiModelProperty(name="netWeight" ,value="净重")
    @Excel(name = "净重", height = 20, width = 30)
    private BigDecimal netWeight;

    @Transient
    @ApiModelProperty(name="grossWeight" ,value="毛重")
    @Excel(name = "毛重", height = 20, width = 30)
    private BigDecimal grossWeight;

    @Transient
    @ApiModelProperty(name = "inventoryStatusName",value = "库存状态")
    private String inventoryStatusName;

    /**
     * 月台名称
     */
    @Transient
    @ApiModelProperty(name = "platformName",value = "月台")
    private String platformName;
}
