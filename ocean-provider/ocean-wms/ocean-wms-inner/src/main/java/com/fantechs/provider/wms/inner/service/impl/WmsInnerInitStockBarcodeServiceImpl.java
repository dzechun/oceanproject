package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInitStockBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/01.
 */
@Service
public class WmsInnerInitStockBarcodeServiceImpl extends BaseService<WmsInnerInitStockBarcode> implements WmsInnerInitStockBarcodeService {

    @Resource
    private WmsInnerInitStockBarcodeMapper wmsInnerInitStockBarcodeMapper;

    @Override
    public List<WmsInnerInitStockBarcode> findList(Map<String, Object> map) {
        return wmsInnerInitStockBarcodeMapper.findList(map);
    }
}
