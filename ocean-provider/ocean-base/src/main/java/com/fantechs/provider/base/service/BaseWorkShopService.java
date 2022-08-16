package com.fantechs.provider.base.service;



import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkShopImport;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
public interface BaseWorkShopService extends IService<BaseWorkShop> {
    List<BaseWorkShopDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseWorkShopImport> baseWorkShopImports);

    List<BaseWorkShop> batchAdd(List<BaseWorkShop> baseWorkShops);
}
