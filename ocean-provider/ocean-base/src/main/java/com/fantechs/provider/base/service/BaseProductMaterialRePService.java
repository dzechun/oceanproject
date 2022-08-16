package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductMaterialReP;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/28.
 */

public interface BaseProductMaterialRePService extends IService<BaseProductMaterialReP> {
    List<BaseProductMaterialReP> findList(Map<String, Object> map);

    List<BaseHtProductMaterialReP> findHtList(Map<String, Object> map);
}
