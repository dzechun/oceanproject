package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.imports.BaseStorageTaskPointImport;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageTaskPoint;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/09.
 */

public interface BaseStorageTaskPointService extends IService<BaseStorageTaskPoint> {
    List<BaseStorageTaskPoint> findList(Map<String, Object> map);
    List<BaseHtStorageTaskPoint> findHtList(Map<String, Object> map);
    Map<String, Object> importExcel(List<BaseStorageTaskPointImport> baseStorageTaskPointImports);
}
