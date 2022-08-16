package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtKeyMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/24.
 */

public interface BaseHtKeyMaterialService extends IService<BaseHtKeyMaterial> {

    List<BaseHtKeyMaterial> findHtList(Map<String, Object> map);
}
