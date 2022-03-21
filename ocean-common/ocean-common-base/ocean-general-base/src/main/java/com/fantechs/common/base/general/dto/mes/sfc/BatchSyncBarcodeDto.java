package com.fantechs.common.base.general.dto.mes.sfc;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchSyncBarcodeDto implements Serializable {

    private List<BatchSyncBarcodeSaveDto> list;

    private List<MesSfcBarcodeProcess> updateList;
}
