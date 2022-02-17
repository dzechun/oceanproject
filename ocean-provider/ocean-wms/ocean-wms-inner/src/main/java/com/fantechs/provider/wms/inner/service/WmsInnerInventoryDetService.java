package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/06/02.
 */

public interface WmsInnerInventoryDetService extends IService<WmsInnerInventoryDet> {
    List<WmsInnerInventoryDetDto> findList(Map<String, Object> map);

    int add(List<WmsInnerInventoryDet> wmsInnerInventoryDets);

    int subtract(WmsInnerInventoryDetDto wmsInnerInventoryDetDto);

    WmsInnerInventoryDet findByOne(String barCode);

    List<WmsInnerInventoryDetDto> findListByBarCode(List<String> codes);

}
