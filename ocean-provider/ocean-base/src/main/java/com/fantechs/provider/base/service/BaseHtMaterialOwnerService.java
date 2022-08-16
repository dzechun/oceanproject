package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/23.
 */

public interface BaseHtMaterialOwnerService extends IService<BaseHtMaterialOwner> {
    List<BaseHtMaterialOwner> findHtList(Map<String, Object> map);
}
