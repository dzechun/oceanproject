package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PDAWmsInnerDirectTransferOrderDto implements Serializable {

    /**
     * 移入库位ID
     */
    @ApiModelProperty(name="inStorageId",value = "移入库位ID")
    private Long inStorageId;

    /**
     * 移出库位ID
     */
    @ApiModelProperty(name="outStorageId",value = "移出库位ID")
    private Long outStorageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 作业人员
     */
    @ApiModelProperty(name="workerUserName",value = "作业人员")
    private Long workerUserId;


    List<PDAWmsInnerDirectTransferOrderDetDto> pdaWmsInnerDirectTransferOrderDetDtos;


    private static final long serialVersionUID = 1L;
}
