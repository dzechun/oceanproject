package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseProductionKeyIssues;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/10.
 */
public interface BaseProductionKeyIssuesService extends IService<BaseProductionKeyIssues> {
    List<BaseProductionKeyIssues> findList(Map<String, Object> map);
}
