package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamHtWiReleaseDto;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiRelease;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */

public interface EamHtWiReleaseService extends IService<EamHtWiRelease> {
    List<EamHtWiReleaseDto> findHtList(Map<String, Object> map);
}