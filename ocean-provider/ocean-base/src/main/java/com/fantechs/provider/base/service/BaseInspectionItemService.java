package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseInspectionItemDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionItemImport;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/03.
 */

public interface BaseInspectionItemService extends IService<BaseInspectionItem> {
    List<BaseInspectionItem> findList(Map<String, Object> map);
    List<BaseInspectionItem> findDetList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseInspectionItemImport> baseInspectionItemImports);
}
