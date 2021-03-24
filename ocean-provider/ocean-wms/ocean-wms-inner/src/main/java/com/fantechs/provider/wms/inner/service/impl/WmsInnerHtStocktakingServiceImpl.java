package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktaking;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtStocktakingMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerHtStocktakingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/22.
 */
@Service
public class WmsInnerHtStocktakingServiceImpl extends BaseService<WmsInnerHtStocktaking> implements WmsInnerHtStocktakingService {

    @Resource
    private WmsInnerHtStocktakingMapper wmsInnerHtStocktakingMapper;

    @Override
    public List<WmsInnerHtStocktaking> findHtList(Map<String, Object> map) {
        return wmsInnerHtStocktakingMapper.findHtList(map);
    }
}
