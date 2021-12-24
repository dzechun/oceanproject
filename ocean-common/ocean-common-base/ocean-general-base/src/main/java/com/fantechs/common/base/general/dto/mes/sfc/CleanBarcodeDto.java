package com.fantechs.common.base.general.dto.mes.sfc;

import lombok.Data;

import java.io.Serializable;

@Data
public class CleanBarcodeDto implements Serializable {

    private String orderBarCode;

    private String salesBarcode;

    private String cutsomerBarcode;
}
