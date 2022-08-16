package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseCurrencyDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseCurrencyImport;
import com.fantechs.common.base.general.entity.basic.BaseCurrency;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/13.
 */

public interface BaseCurrencyService extends IService<BaseCurrency> {

    List<BaseCurrencyDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseCurrencyImport> baseCurrencyImports);
}
