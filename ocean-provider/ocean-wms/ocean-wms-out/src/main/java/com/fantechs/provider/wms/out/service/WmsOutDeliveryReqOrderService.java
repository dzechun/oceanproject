package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutDeliveryReqOrderImport;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryReqOrder;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */

public interface WmsOutDeliveryReqOrderService extends IService<WmsOutDeliveryReqOrder> {
    List<WmsOutDeliveryReqOrderDto> findList(Map<String, Object> map);
    List<WmsOutHtDeliveryReqOrder> findHtList(Map<String, Object> map);
    int save(WmsOutDeliveryReqOrderDto record);
    int update(WmsOutDeliveryReqOrderDto entity);
    int pushDown(List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos);
    Map<String, Object> importExcel(List<WmsOutDeliveryReqOrderImport> wmsOutDeliveryReqOrderImports);
    int updatePutawayQty(Long planDeliveryOrderDetId, BigDecimal putawayQty);
}
