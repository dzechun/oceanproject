package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionExemptedListImport;
import com.fantechs.common.base.general.entity.basic.BaseInspectionExemptedList;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/21.
 */

public interface BaseInspectionExemptedListService extends IService<BaseInspectionExemptedList> {
    List<BaseInspectionExemptedList> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseInspectionExemptedListImport> baseInspectionExemptedListImports);
}
