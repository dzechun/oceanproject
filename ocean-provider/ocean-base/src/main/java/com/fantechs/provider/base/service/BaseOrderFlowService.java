package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/07.
 */

public interface BaseOrderFlowService extends IService<BaseOrderFlow> {
    List<BaseOrderFlowDto> findList(Map<String, Object> map);
}
