package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDetBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderDetBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * Created by Mr.Lei on 2021/05/07.
 */
@Service
public class WmsInnerJobOrderDetBarcodeServiceImpl extends BaseService<WmsInnerJobOrderDetBarcode> implements WmsInnerJobOrderDetBarcodeService {

    @Resource
    private WmsInnerJobOrderDetBarcodeMapper wmsInPutawayOrderDetBarcodeMapper;

    @Override
    public WmsInnerJobOrderDetBarcode findBarCode(String barCode) {
        WmsInnerJobOrderDetBarcode wmsInPutawayOrderDetBarcode =  wmsInPutawayOrderDetBarcodeMapper.selectOne(WmsInnerJobOrderDetBarcode.builder()
                .barcode(barCode)
                .build());
        if(!StringUtils.isEmpty(wmsInPutawayOrderDetBarcode)){
            throw new BizErrorException("");
        }
        return wmsInPutawayOrderDetBarcode;
    }
}
