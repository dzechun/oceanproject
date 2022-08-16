package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.WanbaoErpLogicDto;
import com.fantechs.common.base.general.entity.basic.WanbaoErpLogic;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2022/03/03.
 */

public interface WanbaoErpLogicService extends IService<WanbaoErpLogic> {
    List<WanbaoErpLogicDto> findList(Map<String, Object> map);
}
