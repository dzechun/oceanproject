package com.fantechs.common.base.general.dto.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MesSfcBarcodeProcessRecordDto extends MesSfcBarcodeProcessRecord implements Serializable {
    /**
     * 处理人
     */
    @ApiModelProperty(name="operatorUserName",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="11")
    private String operatorUserName;
}
