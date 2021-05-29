package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */
@Service
public class WmsInnerStockOrderDetServiceImpl extends BaseService<WmsInnerStockOrderDet> implements WmsInnerStockOrderDetService {

    @Resource
    private WmsInnerStockOrderDetMapper wmsInventoryVerificationDetMapper;

    @Override
    public List<WmsInnerStockOrderDetDto> findList(Map<String, Object> map) {
        return wmsInventoryVerificationDetMapper.findList(map);
    }
}
