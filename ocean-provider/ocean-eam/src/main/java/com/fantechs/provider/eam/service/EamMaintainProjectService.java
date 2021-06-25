package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamMaintainProject;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtMaintainProject;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface EamMaintainProjectService extends IService<EamMaintainProject> {
    List<EamMaintainProjectDto> findList(Map<String, Object> map);
    List<EamHtMaintainProject> findHtList(Map<String, Object> map);
}
