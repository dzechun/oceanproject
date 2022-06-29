package com.fantechs.common.base.general.dto.wms.inner;

import com.fantechs.common.base.general.dto.mes.sfc.CleanBarcodeDto;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2022/6/28
 */
@Data
public class OutCleanBarcodeDto extends CleanBarcodeDto implements Serializable {
    private String po;

    private String saleCode;
}
