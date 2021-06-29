package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamReturnOrderDto;
import com.fantechs.common.base.general.entity.eam.EamReturnOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */

public interface EamReturnOrderService extends IService<EamReturnOrder> {
    List<EamReturnOrderDto> findList(Map<String, Object> map);
}
