package com.fantechs.common.base.electronic.entity.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPtlElectronicTagStorage extends BaseQuery implements Serializable {

    /**
     * 储位id
     */
    @ApiModelProperty(name = "storageId",value = "储位id")
    private String storageId;
    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    private String storageName;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    /**
     * 设备标签id（电子标签控制器）
     */
    @ApiModelProperty(name="equipmentTagId",value = "设备标签id（电子标签控制器）")
    private String equipmentTagId;

    /**
     * 设备id（电子标签控制器）
     */
    @ApiModelProperty(name="equipment_id",value = "设备id（电子标签控制器）")
    private Long equipmentId;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipment_name",value = "设备名称")
    private String equipmentName;

    /**
     * 设备ip
     */
    @ApiModelProperty(name="equipment_ip",value = "设备ip")
    private String equipmentIp;

    /**
     * 电子标签id
     */
    @ApiModelProperty(name="electronicTagId",value = "电子标签id")
    private String electronicTagId;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode",value = "储位编码")
    private String storageCode;

    /**
     * 物料id
     */
    @ApiModelProperty(name = "materialId",value = "物料Id")
    private String materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;
}
