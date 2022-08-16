package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSafeStockDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseSafeStockImport;
import com.fantechs.common.base.general.entity.basic.BaseSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSafeStock;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/03/04.
 */

public interface BaseSafeStockService extends IService<BaseSafeStock> {
    List<BaseSafeStockDto> findList(Map<String, Object> map);
    List<BaseSafeStockDto> findHtList(Map<String, Object> map);
    int  inventeryWarning();
    Map<String, Object> importExcel(List<BaseSafeStockImport> baseSafeStockImports);
}
