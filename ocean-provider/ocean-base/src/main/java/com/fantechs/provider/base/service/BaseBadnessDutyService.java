package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBadnessDutyDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessDuty;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/08.
 */

public interface BaseBadnessDutyService extends IService<BaseBadnessDuty> {
    List<BaseBadnessDutyDto> findList(Map<String, Object> map);
}
