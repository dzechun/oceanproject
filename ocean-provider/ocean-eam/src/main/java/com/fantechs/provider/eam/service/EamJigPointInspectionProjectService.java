package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionProjectDto;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionProject;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */

public interface EamJigPointInspectionProjectService extends IService<EamJigPointInspectionProject> {
    List<EamJigPointInspectionProjectDto> findList(Map<String, Object> map);
}
