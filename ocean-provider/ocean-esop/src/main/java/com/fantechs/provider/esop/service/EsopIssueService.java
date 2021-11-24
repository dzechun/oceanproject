package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopIssueDto;
import com.fantechs.common.base.general.entity.esop.EsopIssue;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */

public interface EsopIssueService extends IService<EsopIssue> {
    List<EsopIssueDto> findList(Map<String, Object> map);

    int batchAdd(List<EsopIssue> EsopIssues);

    EsopIssue selectByKey(Long key);
}
