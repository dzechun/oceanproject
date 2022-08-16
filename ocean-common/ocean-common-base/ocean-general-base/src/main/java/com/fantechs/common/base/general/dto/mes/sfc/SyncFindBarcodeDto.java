package com.fantechs.common.base.general.dto.mes.sfc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SyncFindBarcodeDto implements Serializable {

    private List<SyncBarcodeProcessDto> barcodeProcesses;
}
