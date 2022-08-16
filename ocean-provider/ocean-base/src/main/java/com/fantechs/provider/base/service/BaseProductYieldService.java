package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseProductYieldDto;
import com.fantechs.common.base.general.entity.basic.BaseProductYield;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/20.
 */

public interface BaseProductYieldService extends IService<BaseProductYield> {
    List<BaseProductYieldDto> findList(Map<String, Object> map);
    int save(BaseProductYieldDto baseProductYieldDto);
    int update(BaseProductYieldDto baseProductYieldDto);
}
