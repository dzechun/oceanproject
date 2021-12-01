package com.fantechs.provider.guest.meidi.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MeterialPrepation implements Serializable {


    @ApiModelProperty(name="storageCode" ,value="库位编码")
    private String storageCode;

    @ApiModelProperty(name="type" ,value="操作类型(1-完成备料，2-取消备料)")
    private String type;
}
