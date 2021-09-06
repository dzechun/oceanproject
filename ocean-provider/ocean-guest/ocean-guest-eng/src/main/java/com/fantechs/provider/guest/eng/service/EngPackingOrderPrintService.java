package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintParam;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/9/4
 */
public interface EngPackingOrderPrintService {
    List<EngPackingOrderPrintDto> findList(Map<String,Object> map);

    int print(EngPackingOrderPrintParam engPackingOrderPrintParam);
}
