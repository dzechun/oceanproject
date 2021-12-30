package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombineCartonPalletInfoDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombineCheckBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombinePrintDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;

/**
 * 拣货单
 * @Author mr.lei
 * @Date 2021/5/10
 */
public interface PDAWmsInnerSplitAndCombineCartonPalletService {
    PDAWmsInnerSplitAndCombineCartonPalletInfoDto getCartonPalletInfo(String barcode, Byte type);

    WmsInnerInventoryDetDto checkBarcode(PDAWmsInnerSplitAndCombineCheckBarcodeDto pdaWmsInnerSplitAndCombineCheckBarcodeDto);

    BaseStorage checkStorageCode(String storageCode);

    String print(PDAWmsInnerSplitAndCombinePrintDto pdaWmsInnerSplitAndCombinePrintDto);
}
