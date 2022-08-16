package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtPlatform;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/14.
 */

public interface BaseHtPlatformService extends IService<BaseHtPlatform> {
    List<BaseHtPlatform> findHtList(Map<String, Object> map);
}
