package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamHtWiReleaseDetDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiReleaseDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */

public interface EamHtWiReleaseDetService extends IService<EamHtWiReleaseDet> {
    List<EamHtWiReleaseDetDto> findList(Map<String, Object> map);
}
