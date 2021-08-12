package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProject;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/12.
 */

public interface EamHtJigMaintainProjectService extends IService<EamHtJigMaintainProject> {
    List<EamHtJigMaintainProject> findHtList(Map<String, Object> map);
}
