package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;

import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderDetBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/28.
 */
@Service
public class WmsInnerStockOrderDetBarcodeServiceImpl extends BaseService<WmsInnerStockOrderDetBarcode> implements WmsInnerStockOrderDetBarcodeService {

    @Resource
    private WmsInnerStockOrderDetBarcodeMapper wmsInnerStockOrderDetBarcodeMapper;

    @Override
    public List<WmsInnerStockOrderDetBarcodeDto> findList(Map<String, Object> map) {
        return wmsInnerStockOrderDetBarcodeMapper.findList(map);
    }


}
