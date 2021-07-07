package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtIssue;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */

public interface EamHtIssueService extends IService<EamHtIssue> {
    List<EamHtIssue> findHtList(Map<String, Object> map);
}
