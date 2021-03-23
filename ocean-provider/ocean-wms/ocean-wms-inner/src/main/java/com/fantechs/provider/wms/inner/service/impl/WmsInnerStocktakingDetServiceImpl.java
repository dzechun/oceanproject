package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktakingDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStocktakingDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStocktakingDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/22.
 */
@Service
public class WmsInnerStocktakingDetServiceImpl extends BaseService<WmsInnerStocktakingDet> implements WmsInnerStocktakingDetService {

    @Resource
    private WmsInnerStocktakingDetMapper wmsInnerStocktakingDetMapper;

    @Override
    public List<WmsInnerStocktakingDetDto> findList(Map<String, Object> map) {
        return wmsInnerStocktakingDetMapper.findList(map);
    }
}
