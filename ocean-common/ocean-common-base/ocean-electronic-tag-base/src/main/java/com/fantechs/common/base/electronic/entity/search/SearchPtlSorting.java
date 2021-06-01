package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPtlSorting extends BaseQuery implements Serializable {

    @ApiModelProperty(name="sortingId",value = "分拣单Id")
    private Long sortingId;

    @ApiModelProperty(name="sortingCode",value = "任务号")
    private String sortingCode;

    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;

    @ApiModelProperty(name="orderType",value = "单据类型（1-调拨单 2领料单）")
    private Byte orderType;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(name="warehouseAreaId",value = "仓库区域Id")
    private Long warehouseAreaId;

    @ApiModelProperty(name = "storageName",value = "储位名称")
    private String storageName;

    @ApiModelProperty(name="storageCode",value = "储位编码")
    private String storageCode;

    @ApiModelProperty(name = "workerName",value = "作业人员")
    private String workerName;

    @ApiModelProperty(name="electronicTagId",value = "电子标签Id")
    private String electronicTagId;

    @ApiModelProperty(name="equipmentId",value = "电子标签Id")
    private String equipmentId;

    @ApiModelProperty(name="equipmentAreaId",value = "区域设备Id")
    private String equipmentAreaId;

    @ApiModelProperty(name="orderBySortingCode",value = "分拣单筛选分组")
    private String groupBySortingCode;

    @ApiModelProperty(name="notEqualSortingCode",value = "不包含分拣单号")
    private String notEqualSortingCode;

    @ApiModelProperty(name="statusList",value = "不等于该状态（0-未开始 1-分拣中 2-完成）")
    private Byte notEqualstatus;
}
