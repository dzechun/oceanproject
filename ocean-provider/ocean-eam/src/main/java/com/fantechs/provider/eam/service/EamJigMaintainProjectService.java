package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProject;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/12.
 */

public interface EamJigMaintainProjectService extends IService<EamJigMaintainProject> {
    List<EamJigMaintainProjectDto> findList(Map<String, Object> map);
}
