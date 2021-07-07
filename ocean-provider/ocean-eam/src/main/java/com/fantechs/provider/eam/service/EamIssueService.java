package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamIssueDto;
import com.fantechs.common.base.general.entity.eam.EamIssue;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */

public interface EamIssueService extends IService<EamIssue> {
    List<EamIssueDto> findList(Map<String, Object> map);
}
