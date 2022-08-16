package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.imports.BaseProductProcessReMImport;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessReM;
import com.fantechs.common.base.support.IService;


import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/28.
 */

public interface BaseProductProcessReMService extends IService<BaseProductProcessReM> {
    List<BaseProductProcessReM> findList(Map<String, Object> map);

    List<BaseHtProductProcessReM> findHtList(Map<String, Object> map);

    Map<String, Object> importExcel(List<BaseProductProcessReMImport> baseProductProcessReMImports);
}
