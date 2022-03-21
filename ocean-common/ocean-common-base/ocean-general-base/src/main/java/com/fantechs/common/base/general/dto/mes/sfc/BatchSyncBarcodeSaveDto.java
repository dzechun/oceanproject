package com.fantechs.common.base.general.dto.mes.sfc;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import lombok.Data;

import java.io.Serializable;

@Data
public class BatchSyncBarcodeSaveDto implements Serializable {

    private MesSfcWorkOrderBarcode workOrderBarcode;

    private MesSfcBarcodeProcess barcodeProcess;
}
