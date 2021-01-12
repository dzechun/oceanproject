package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WmsOutShippingNoteDetDto extends WmsOutShippingNoteDet implements Serializable {

    @Excel(name = "成品编码", height = 20, width = 30,orderNum="1")
    @ApiModelProperty(name="productMaterialCode" ,value="成品编码")
    private String productMaterialCode;

    @Excel(name = "版本", height = 20, width = 30,orderNum="2")
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

    @Excel(name = "成品描述", height = 20, width = 30,orderNum="3")
    @ApiModelProperty(name="productMaterialName" ,value="成品描述（规格？）")
    private String productMaterialDesc;

    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="4")
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="5")
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    @Excel(name = "储位名称", height = 20, width = 30,orderNum="6")
    @ApiModelProperty(name="storageName" ,value="储位名称")
    private String storageName;

    @Excel(name = "调入仓库名称", height = 20, width = 30,orderNum="7")
    @ApiModelProperty(name="moveWarehouseName" ,value="调入仓库名称")
    private String moveWarehouseName;

    @Excel(name = "调入仓库区域名称", height = 20, width = 30,orderNum="8")
    @ApiModelProperty(name="moveWarehouseAreaName" ,value="调入仓库区域名称")
    private String moveWarehouseAreaName;

    @Excel(name = "调入储位名称", height = 20, width = 30,orderNum="9")
    @ApiModelProperty(name="moveStorageName" ,value="调入储位名称")
    private String moveStorageName;


}
