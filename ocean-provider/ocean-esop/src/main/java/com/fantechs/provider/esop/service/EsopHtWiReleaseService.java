package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopHtWiReleaseDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiRelease;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */

public interface EsopHtWiReleaseService extends IService<EsopHtWiRelease> {
    List<EsopHtWiReleaseDto> findHtList(Map<String, Object> map);
}
