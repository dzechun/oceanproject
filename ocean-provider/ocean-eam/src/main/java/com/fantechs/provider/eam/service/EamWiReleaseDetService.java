package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamWiReleaseDetDto;
import com.fantechs.common.base.general.entity.eam.EamWiReleaseDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiReleaseDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/08.
 */

public interface EamWiReleaseDetService extends IService<EamWiReleaseDet> {
    List<EamWiReleaseDetDto> findList(SearchEamWiReleaseDet searchEamWiReleaseDet);
}
