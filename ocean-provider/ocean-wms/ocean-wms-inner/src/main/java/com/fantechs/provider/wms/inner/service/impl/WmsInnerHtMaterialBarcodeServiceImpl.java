package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerHtMaterialBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtMaterialBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerHtMaterialBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/03.
 */
@Service
public class WmsInnerHtMaterialBarcodeServiceImpl extends BaseService<WmsInnerHtMaterialBarcode> implements WmsInnerHtMaterialBarcodeService {

    @Resource
    private WmsInnerHtMaterialBarcodeMapper wmsInnerHtMaterialBarcodeMapper;

   /* @Override
    public List<WmsInnerHtMaterialBarcodeDto> findList(Map<String, Object> map) {
        return wmsInnerHtMaterialBarcodeMapper.findList(map);
    }*/
}
