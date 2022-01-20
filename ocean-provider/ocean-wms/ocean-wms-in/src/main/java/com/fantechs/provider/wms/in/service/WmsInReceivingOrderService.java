package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderBarcode;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInReceivingOrderImport;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/13.
 */

public interface WmsInReceivingOrderService extends IService<WmsInReceivingOrder> {
    List<WmsInReceivingOrderDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<WmsInReceivingOrderImport> list);

    int pushDown(String ids);


    List<WmsInReceivingOrderBarcode> scanBarcode(String barcode);
}
