package com.fantechs.common.base.general.entity.smt.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SearchSmtMaterialLifetime extends BaseQuery implements Serializable {

    /**
     * MSD湿度等级
     */
    @ApiModelProperty(name="theHumidityLevel",value = "MSD湿度等级")
    private String theHumidityLevel;

    /**
     * 密封保存时间(年)
     */
    @ApiModelProperty(name="sealHoldingTime",value = "密封保存时间(年)")
    private BigDecimal sealHoldingTime;

    /**
     * 拆封保存时间(小时)
     */
    @ApiModelProperty(name="openHoldTime",value = "拆封保存时间(小时)")
    private BigDecimal openHoldTime;

}
