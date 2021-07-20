package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamWiReleaseDto;
import com.fantechs.common.base.general.dto.eam.EamWorkInstructionDto;
import com.fantechs.common.base.general.entity.eam.EamWiRelease;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiRelease;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/08.
 */

public interface EamWiReleaseService extends IService<EamWiRelease> {
    List<EamWiReleaseDto> findList(SearchEamWiRelease searchEamWiRelease);
    int save(EamWiReleaseDto eamWiReleaseDto);

    int update(EamWiReleaseDto eamWiReleaseDto);

    int censor(EamWiReleaseDto eamWiReleaseDto);

}
