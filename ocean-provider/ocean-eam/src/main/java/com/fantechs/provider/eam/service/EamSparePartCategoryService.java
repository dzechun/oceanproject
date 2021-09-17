package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.EamSparePartCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtSparePartCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/17.
 */

public interface EamSparePartCategoryService extends IService<EamSparePartCategory> {
    List<EamSparePartCategory> findList(Map<String, Object> map);

    List<EamHtSparePartCategory> findHtList(Map<String, Object> map);
}
