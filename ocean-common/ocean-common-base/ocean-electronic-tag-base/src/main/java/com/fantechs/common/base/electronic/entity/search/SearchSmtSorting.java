package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtSorting extends BaseQuery implements Serializable {

    /**
     * 分拣单Id
     */
    @ApiModelProperty(name="sortingId",value = "分拣单Id")
    private Long sortingId;

    /**
     * 分拣单号
     */
    @ApiModelProperty(name="sortingCode",value = "分拣单号")
    private String sortingCode;

    /**
     * 单据类型（1-调拨单 2领料单）
     */
    @ApiModelProperty(name="orderType",value = "单据类型（1-调拨单 2领料单）")
    private Byte orderType;

    /**
     * 工单号(预留)
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号(预留)")
    private String workOrderCode;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode",value = "储位编码")
    private String storageCode;

    /**
     * 电子标签Id
     */
    @ApiModelProperty(name="electronicTagId",value = "电子标签Id")
    private String electronicTagId;

    /**
     * 设备Id
     */
    @ApiModelProperty(name="equipmentId",value = "电子标签Id")
    private String equipmentId;

    /**
     * 区域设备Id
     */
    @ApiModelProperty(name="equipmentAreaId",value = "区域设备Id")
    private String equipmentAreaId;

    /**
     * 分拣单筛选分组
     */
    @ApiModelProperty(name="orderBySortingCode",value = "分拣单筛选分组")
    private String orderBySortingCode;

    /**
     * 不包含该分拣单号
     */
    @ApiModelProperty(name="notEqualSortingCode",value = "不包含分拣单号")
    private String notEqualSortingCode;

    /**
     * 不等于该状态（0-未开始 1-分拣中 2-完成）
     */
    @ApiModelProperty(name="statusList",value = "不等于该状态（0-未开始 1-分拣中 2-完成）")
    private Byte notEqualstatus;
}
