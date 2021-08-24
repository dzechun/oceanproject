package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDetDto;
import com.fantechs.common.base.general.entity.esop.EsopWiReleaseDet;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiReleaseDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/08.
 */

public interface EsopWiReleaseDetService extends IService<EsopWiReleaseDet> {
    List<EsopWiReleaseDetDto> findList(SearchEsopWiReleaseDet searchEsopWiReleaseDet);
}
