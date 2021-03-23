package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktakingDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtStocktakingDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerHtStocktakingDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/22.
 */
@Service
public class WmsInnerHtStocktakingDetServiceImpl extends BaseService<WmsInnerHtStocktakingDet> implements WmsInnerHtStocktakingDetService {

    @Resource
    private WmsInnerHtStocktakingDetMapper wmsInnerHtStocktakingDetMapper;

    @Override
    public List<WmsInnerHtStocktakingDet> findHtList(Map<String, Object> map) {
        return wmsInnerHtStocktakingDetMapper.findHtList(map);
    }
}
