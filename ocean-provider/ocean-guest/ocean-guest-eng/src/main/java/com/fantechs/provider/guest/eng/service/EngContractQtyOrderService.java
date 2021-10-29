package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderDto;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */

public interface EngContractQtyOrderService extends IService<EngContractQtyOrder> {
    List<EngContractQtyOrderDto> findList(Map<String, Object> map);

    int saveByApi (EngContractQtyOrder engContractQtyOrder);

}
