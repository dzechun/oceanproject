package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductionKeyIssues;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/10.
 */

public interface BaseHtProductionKeyIssuesService extends IService<BaseHtProductionKeyIssues> {
    List<BaseHtProductionKeyIssues> findHtList(Map<String, Object> map);
}
