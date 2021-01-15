package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class WmsOutShippingNoteDetDto extends WmsOutShippingNoteDet implements Serializable {

    @Excel(name = "版本", height = 20, width = 30,orderNum="2")
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

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

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;
}
