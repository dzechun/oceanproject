package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WmsOutProductionMaterialDto extends WmsOutProductionMaterial implements Serializable {

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="2")
    private String workOrderCode;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proLineName" ,value="产线名称")
    @Excel(name = "产线名称", height = 20, width = 30,orderNum="3")
    private String proLineName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="4")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="5")
    private String materialName;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="9")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="10")
    private String warehouseName;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode" ,value="储位编码")
    @Excel(name = "储位编码", height = 20, width = 30,orderNum="11")
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name="storageName" ,value="储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum="12")
    private String storageName;



}
