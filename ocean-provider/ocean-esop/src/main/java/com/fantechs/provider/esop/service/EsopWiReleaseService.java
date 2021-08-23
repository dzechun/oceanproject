package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDto;
import com.fantechs.common.base.general.entity.esop.EsopWiRelease;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiRelease;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/08.
 */

public interface EsopWiReleaseService extends IService<EsopWiRelease> {
    List<EsopWiReleaseDto> findList(SearchEsopWiRelease searchEsopWiRelease);
    int save(EsopWiReleaseDto EsopWiReleaseDto);

    int update(EsopWiReleaseDto EsopWiReleaseDto);

    int censor(EsopWiRelease EsopWiRelease);

}
