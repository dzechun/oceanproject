package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigPointInspectionProject;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */

public interface EamHtJigPointInspectionProjectService extends IService<EamHtJigPointInspectionProject> {
    List<EamHtJigPointInspectionProject> findHtList(Map<String, Object> map);
}
