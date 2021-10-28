package com.fantechs.common.base.general.dto.daq;

import com.fantechs.common.base.general.entity.daq.DaqDataCollect;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaqDataCollectDto extends DaqDataCollect implements Serializable {

    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(name="equipmentName",value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(name="equipmentIp",value = "设备IP")
    private String equipmentIp;

    @ApiModelProperty(name="xaxis",value = "X坐标")
    private BigDecimal xaxis;

    @ApiModelProperty(name="yaxis",value = "Y坐标")
    private BigDecimal yaxis;

    @ApiModelProperty(name="tableName",value = "表头")
    private String tableName;

}
