package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerStockOrderImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */

public interface WmsInnerStockOrderDetService extends IService<WmsInnerStockOrderDet> {
    List<WmsInnerStockOrderDetDto> findList(Map<String, Object> map);

    Map<String,Object> importExcel(Long stockOrderId,List<WmsInnerStockOrderImport> wmsInnerStockOrderImports);
}
