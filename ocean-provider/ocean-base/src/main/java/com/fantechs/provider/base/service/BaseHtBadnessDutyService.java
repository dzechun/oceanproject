package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessDuty;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/08.
 */

public interface BaseHtBadnessDutyService extends IService<BaseHtBadnessDuty> {
    List<BaseHtBadnessDuty> findList(Map<String, Object> map);
}
