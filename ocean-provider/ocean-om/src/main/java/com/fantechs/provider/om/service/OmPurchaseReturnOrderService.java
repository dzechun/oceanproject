package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDto;
import com.fantechs.common.base.general.dto.om.imports.OmPurchaseReturnOrderImport;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseReturnOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/20.
 */

public interface OmPurchaseReturnOrderService extends IService<OmPurchaseReturnOrder> {
    List<OmPurchaseReturnOrderDto> findList(Map<String, Object> map);
    List<OmHtPurchaseReturnOrder> findHtList(Map<String, Object> map);
    int pushDown(List<OmPurchaseReturnOrderDetDto> omPurchaseReturnOrderDetDtos);
    int save(OmPurchaseReturnOrderDto record);
    int update(OmPurchaseReturnOrderDto entity);
    Map<String, Object> importExcel(List<OmPurchaseReturnOrderImport> omPurchaseReturnOrderImports);
}
