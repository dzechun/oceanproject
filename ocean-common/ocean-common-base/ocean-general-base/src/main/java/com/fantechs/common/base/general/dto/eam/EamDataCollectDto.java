package com.fantechs.common.base.general.dto.eam;

import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EamDataCollectDto extends EamDataCollect implements Serializable {

    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(name="equipmentName",value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(name="equipmentIp",value = "设备IP")
    private String equipmentIp;

    @ApiModelProperty(name="xAxis",value = "X坐标")
    private BigDecimal xAxis;

    @ApiModelProperty(name="yAxis",value = "Y坐标")
    private BigDecimal yAxis;

    @ApiModelProperty(name="tableName",value = "表头")
    private String tableName;

}
