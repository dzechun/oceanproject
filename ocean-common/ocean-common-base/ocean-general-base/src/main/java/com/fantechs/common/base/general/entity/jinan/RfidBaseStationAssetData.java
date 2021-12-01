package com.fantechs.common.base.general.entity.jinan;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class RfidBaseStationAssetData implements Serializable {

    /**
     * RFID序列号
     */
    @ApiModelProperty(name="assetBarcode",value = "RFID序列号")
    private String assetBarcode;

    /**
     * 读取时间
     */
    @ApiModelProperty(name="readTime",value = "读取时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date readTime;
}
