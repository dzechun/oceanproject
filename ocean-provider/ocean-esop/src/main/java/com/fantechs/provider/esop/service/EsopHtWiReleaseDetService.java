package com.fantechs.provider.esop.service;

import com.fantechs.common.base.general.dto.esop.EsopHtWiReleaseDetDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiReleaseDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */

public interface EsopHtWiReleaseDetService extends IService<EsopHtWiReleaseDet> {
    List<EsopHtWiReleaseDetDto> findList(Map<String, Object> map);
}
