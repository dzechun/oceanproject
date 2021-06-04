package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/01.
 */

public interface PtlJobOrderService extends IService<PtlJobOrder> {
    List<PtlJobOrderDto> findList(Map<String, Object> map);
}
