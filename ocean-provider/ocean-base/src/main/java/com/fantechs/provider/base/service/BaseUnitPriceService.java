package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseUnitPriceImport;
import com.fantechs.common.base.general.entity.basic.BaseUnitPrice;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/27.
 */

public interface BaseUnitPriceService extends IService<BaseUnitPrice> {
    List<BaseUnitPriceDto> findList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseUnitPriceImport> baseUnitPriceImports);
}
