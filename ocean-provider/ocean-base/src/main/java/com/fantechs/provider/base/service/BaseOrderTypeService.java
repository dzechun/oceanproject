package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseOrderTypeDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderType;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/23.
 */

public interface BaseOrderTypeService extends IService<BaseOrderType> {
    List<BaseOrderTypeDto> findList(Map<String, Object> map);
}
