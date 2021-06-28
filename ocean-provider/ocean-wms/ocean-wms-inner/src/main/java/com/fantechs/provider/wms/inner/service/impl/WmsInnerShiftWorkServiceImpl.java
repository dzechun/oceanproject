package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WmsInnerShiftWorkServiceImpl implements WmsInnerShiftWorkService {

    @Autowired
    WmsInnerJobOrderService wmsInnerJobOrderService;

    @Autowired
    WmsInnerJobOrderDetService wmsInnerJobOrderDetService;

    @Override
    public List<WmsInnerJobOrderDto> pdaFindList(String jobOrderCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("jobOrderType", (byte) 2);
        if(StringUtils.isNotEmpty(jobOrderCode)){
            map.put("jobOrderCode", jobOrderCode);
        }
        wmsInnerJobOrderService.findShiftList(map);
        return null;
    }

    @Override
    public List<WmsInnerJobOrderDetDto> pdaFindDetList(Long jobOrderId) {
        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderId(jobOrderId);
        searchWmsInnerJobOrderDet.setNonShiftStorageStatus((byte) 4);
        return wmsInnerJobOrderDetService.findList(searchWmsInnerJobOrderDet);
    }


}
