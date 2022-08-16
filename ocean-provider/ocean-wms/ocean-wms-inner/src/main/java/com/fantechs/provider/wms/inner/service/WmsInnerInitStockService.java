package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.InitStockCheckBarCode;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.InitStockImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStock;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/01.
 */

public interface WmsInnerInitStockService extends IService<WmsInnerInitStock> {
    List<WmsInnerInitStockDto> findList(Map<String, Object> map);

    InitStockCheckBarCode checkBarCode(String barCode);

    int finish(Long initStockId);

    Map<String ,Object> importExcel(List<InitStockImport> initStockImports);

    int deleteBarCode(Long initStockBarCodeId);
}
