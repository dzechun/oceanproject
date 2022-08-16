package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerHtJobOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtJobOrderDetBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtJobOrderDetBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerHtJobOrderDetBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/01.
 */
@Service
public class WmsInnerHtJobOrderDetBarcodeServiceImpl extends BaseService<WmsInnerHtJobOrderDetBarcode> implements WmsInnerHtJobOrderDetBarcodeService {

    @Resource
    private WmsInnerHtJobOrderDetBarcodeMapper wmsInnerHtJobOrderDetBarcodeMapper;

//    @Override
//    public List<WmsInnerHtJobOrderDetBarcodeDto> findList(Map<String, Object> map) {
//        return wmsInnerHtJobOrderDetBarcodeMapper.findList(map);
//    }
}
