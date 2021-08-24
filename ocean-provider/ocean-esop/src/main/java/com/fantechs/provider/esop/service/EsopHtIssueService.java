package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.entity.esop.history.EsopHtIssue;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */

public interface EsopHtIssueService extends IService<EsopHtIssue> {
    List<EsopHtIssue> findHtList(Map<String, Object> map);
}
