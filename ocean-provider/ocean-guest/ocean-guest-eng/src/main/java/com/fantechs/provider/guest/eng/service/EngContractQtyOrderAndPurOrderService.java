package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */

public interface EngContractQtyOrderAndPurOrderService extends IService<EngContractQtyOrderAndPurOrderDto> {
    List<EngContractQtyOrderAndPurOrderDto> findList(Map<String, Object> map);

}
