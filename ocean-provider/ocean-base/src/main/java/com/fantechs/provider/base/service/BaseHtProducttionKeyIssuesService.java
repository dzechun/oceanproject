package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProducttionKeyIssues;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/10.
 */

public interface BaseHtProducttionKeyIssuesService extends IService<BaseHtProducttionKeyIssues> {
    List<BaseHtProducttionKeyIssues> findHtList(Map<String, Object> map);
}
