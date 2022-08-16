package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDetDto;
import com.fantechs.common.base.general.entity.basic.BaseUnitPriceDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/09.
 */

public interface BaseUnitPriceDetService extends IService<BaseUnitPriceDet> {
    List<BaseUnitPriceDetDto> findList(Map<String, Object> map);
}
