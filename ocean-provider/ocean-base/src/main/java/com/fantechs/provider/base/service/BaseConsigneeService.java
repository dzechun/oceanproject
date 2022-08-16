package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseConsigneeDto;
import com.fantechs.common.base.general.entity.basic.BaseConsignee;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/23.
 */

public interface BaseConsigneeService extends IService<BaseConsignee> {
    List<BaseConsigneeDto> findList(Map<String, Object> map);
}
