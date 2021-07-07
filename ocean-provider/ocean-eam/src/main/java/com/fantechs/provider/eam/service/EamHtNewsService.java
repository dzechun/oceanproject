package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtNews;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */

public interface EamHtNewsService extends IService<EamHtNews> {
    List<EamHtNews> findHtList(Map<String, Object> map);
}
