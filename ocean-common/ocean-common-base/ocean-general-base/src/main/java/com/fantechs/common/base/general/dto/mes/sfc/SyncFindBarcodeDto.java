package com.fantechs.common.base.general.dto.mes.sfc;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SyncFindBarcodeDto implements Serializable {

    private List<MesSfcBarcodeProcess> barcodeProcesses;

    private List<MesSfcWorkOrderBarcode> workOrderBarcodes;
}
