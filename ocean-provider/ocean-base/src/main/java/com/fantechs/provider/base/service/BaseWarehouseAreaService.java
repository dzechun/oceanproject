package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseWarehouseAreaDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseAreaImport;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */

public interface BaseWarehouseAreaService extends IService<BaseWarehouseArea> {

   List<BaseWarehouseAreaDto> findList(Map<String,Object> map);

   Map<String, Object> importExcel(List<BaseWarehouseAreaImport> baseWarehouseAreaImports);
}
