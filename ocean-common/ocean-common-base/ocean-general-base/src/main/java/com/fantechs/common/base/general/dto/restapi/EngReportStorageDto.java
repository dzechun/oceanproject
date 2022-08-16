package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class EngReportStorageDto implements Serializable {

    @Id
    @ApiModelProperty(name="storageId",value = "库位ID")
    private String storageId;

    @ApiModelProperty(name="storageCode",value = "库位编码")
    private String storageCode;

    @ApiModelProperty(name="storageDesc",value = "库位描述")
    private String storageDesc;

    @ApiModelProperty(name="storageType",value = "货架地点类别")
    private String storageType;

}
