package com.fantechs.provider.guest.leisai.service;

import com.fantechs.common.base.general.dto.leisai.LeisaiHtProductAndHalfOrderDto;
import com.fantechs.common.base.general.entity.leisai.history.LeisaiHtProductAndHalfOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/27.
 */

public interface LeisaiHtProductAndHalfOrderService extends IService<LeisaiHtProductAndHalfOrder> {
    List<LeisaiHtProductAndHalfOrderDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<LeisaiHtProductAndHalfOrder> list);
}
