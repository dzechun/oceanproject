package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class BaseStorageImport implements Serializable {

    /**
     * 库位编码(必填)
     */
    @ApiModelProperty(name = "storageCode",value = "库位编码")
    @Excel(name = "库位编码(必填)", height = 20, width = 30)
    private String storageCode;

    /**
     * 库位名称
     */
    @ApiModelProperty(name = "storageName",value = "库位名称")
    @Excel(name = "库位名称", height = 20, width = 30)
    private String storageName;

    /**
     * 层级
     */
    @ApiModelProperty(name = "level",value = "层级")
    @Excel(name = "层级", height = 20, width = 30)
    private String level;

    /**
     * 库位描述
     */
    @ApiModelProperty(name = "storageDesc",value = "库位描述")
    @Excel(name = "库位描述", height = 20, width = 30)
    private String storageDesc;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    //@Excel(name = "仓库编码", height = 20, width = 30)
    private String warehouseCode;

    /**
     * 仓库区域ID
     */
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域ID")
    private Long warehouseAreaId;

    /**
     * 仓库区域编码(必填)
     */
    @ApiModelProperty(name="warehouseAreaCode" ,value="仓库区域编码")
    @Excel(name = "仓库区域编码(必填)", height = 20, width = 30)
    private String warehouseAreaCode;

    /**
     * 容量
     */
    @ApiModelProperty(name="capacity",value = "容量")
    @Excel(name = "容量", height = 20, width = 30)
    private BigDecimal capacity;

    /**
     * 温度
     */
    @ApiModelProperty(name="temperature",value = "温度")
    @Excel(name = "温度", height = 20, width = 30)
    private BigDecimal temperature;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit",value = "单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;

    /**
     * 库位类型(1-存货 2-收货 3-发货)(必填)
     */
    @ApiModelProperty(name="storageType",value = "库位类型(1-存货 2-收货 3-发货)")
    @Excel(name = "库位类型(1-存货 2-收货 3-发货)(必填)", height = 20, width = 30)
    private Integer storageType;

    /**
     * 工作区ID
     */
    @ApiModelProperty(name="workingAreaId",value = "工作区ID")
    private Long workingAreaId;

    /**
     * 工作区编码(必填)
     */
    @ApiModelProperty(name="workingAreaCode" ,value="工作区编码")
    @Excel(name = "工作区编码(必填)", height = 20, width = 30)
    private String workingAreaCode;

    /**
     * 产品存储类型(1-A类 2-B类 3-C类 4-D类)(必填)
     */
    @ApiModelProperty(name="materialStoreType",value = "产品存储类型(1-A类 2-B类 3-C类 4-D类)(必填)")
    @Excel(name = "产品存储类型(1-A类 2-B类 3-C类 4-D类)(必填)", height = 20, width = 30)
    private Integer materialStoreType;

    /**
     * 巷道(必填)
     */
    @ApiModelProperty(name="roadway",value = "巷道")
    @Excel(name = "巷道(必填)", height = 20, width = 30)
    private Integer roadway;

    /**
     * 排(必填)
     */
    @ApiModelProperty(name="rowNo",value = "排")
    @Excel(name = "排(必填)", height = 20, width = 30)
    private Integer rowNo;

    /**
     * 列(必填)
     */
    @ApiModelProperty(name="columnNo",value = "列")
    @Excel(name = "列(必填)", height = 20, width = 30)
    private Integer columnNo;

    /**
     * 层(必填)
     */
    @ApiModelProperty(name="levelNo",value = "层")
    @Excel(name = "层(必填)", height = 20, width = 30)
    private Integer levelNo;

    /**
     * 上架动线号(必填)
     */
    @ApiModelProperty(name="putawayMoveLineNo",value = "上架动线号")
    @Excel(name = "上架动线号(必填)", height = 20, width = 30)
    private Integer putawayMoveLineNo;

    /**
     * 拣货动线号(必填)
     */
    @ApiModelProperty(name="pickingMoveLineNo",value = "拣货动线号")
    @Excel(name = "拣货动线号(必填)", height = 20, width = 30)
    private Integer pickingMoveLineNo;

    /**
     * 盘点动线号(必填)
     */
    @ApiModelProperty(name="stockMoveLineNo",value = "盘点动线号")
    @Excel(name = "盘点动线号(必填)", height = 20, width = 30)
    private Integer stockMoveLineNo;

    /**
     * erp逻辑id
     */
    @ApiModelProperty(name="logicId",value = "erp逻辑id")
    private Long logicId;

    /**
     * ERP逻辑仓库编码(必填)
     */
    @ApiModelProperty(name="logicCode" ,value="ERP逻辑仓库编码(必填)")
    @Excel(name = "ERP逻辑仓库编码(必填)", height = 20, width = 30)
    private String logicCode;

    /**
     * 产线id
     */
    @ApiModelProperty(name="proLineId",value = "产线id")
    private Long proLineId;

    /**
     * 产线编码(必填)
     */
    @ApiModelProperty(name="proCode" ,value="产线编码(必填)")
    @Excel(name = "产线编码(必填)", height = 20, width = 30)
    private String proCode;

    /**
     * 是否底垫(1-否 2-是)(必填)
     */
    @ApiModelProperty(name="isHeelpiece",value = "是否底垫(1-否 2-是)(必填)")
    @Excel(name = "是否底垫(1-否 2-是)(必填)", height = 20, width = 30)
    private Integer isHeelpiece;
}
