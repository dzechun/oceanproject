package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPtlJobOrderDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name="jobOrderDetId",value = "作业单明细ID")
    private Long jobOrderDetId;

    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    private Long jobOrderId;

    @ApiModelProperty(name="storageId",value = "库位ID")
    private Long storageId;

    @ApiModelProperty(name="storageCode",value = "库位编码")
    private String storageCode;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    @ApiModelProperty(name="wholeOrScattered",value = "整或者零(0-零 1-整)")
    private Byte wholeOrScattered;

    @ApiModelProperty(name="jobStatus",value = "作业状态(1-待作业 2-作业中 3-已完成 4-挂起)")
    private Byte jobStatus;

    @ApiModelProperty(name="ifHangUp",value = "是否挂起（0-否 1-是）")
    private Byte ifHangUp;

    @ApiModelProperty(name="equipmentTagId",value = "设备标签ID")
    private String equipmentTagId;

    @ApiModelProperty(name="electronicTagId",value = "电子标签Id")
    private String electronicTagId;

    @ApiModelProperty(name="equipmentAreaId",value = "区域设备Id")
    private String equipmentAreaId;

    @ApiModelProperty(name="jobOrderDet",value = "是否对作业单明细分组（0-否 1-是）")
    private int jobOrderDet = 1;
}
