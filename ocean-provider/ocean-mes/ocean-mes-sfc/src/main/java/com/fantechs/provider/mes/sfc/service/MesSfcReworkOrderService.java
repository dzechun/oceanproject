package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.DoReworkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.GenerateReworkOrderCodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcHtReworkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcReworkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/15.
 */

public interface MesSfcReworkOrderService extends IService<MesSfcReworkOrder> {
    List<MesSfcReworkOrderDto> findList(Map<String, Object> map);

    List<MesSfcHtReworkOrderDto> findHtList(Map<String, Object> map);

    GenerateReworkOrderCodeDto generateReworkOrderCode(SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess);

    int save(DoReworkOrderDto doReworkOrderDto) throws Exception;
}
