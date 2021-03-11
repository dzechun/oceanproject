package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrapDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryScrapDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryScrapDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/10.
 */
@Service
public class WmsInnerInventoryScrapDetServiceImpl extends BaseService<WmsInnerInventoryScrapDet> implements WmsInnerInventoryScrapDetService {

    @Resource
    private WmsInnerInventoryScrapDetMapper wmsInnerInventoryScrapDetMapper;

    @Override
    public List<WmsInnerInventoryScrapDetDto> findList(Map<String, Object> map) {
        return wmsInnerInventoryScrapDetMapper.findList(map);
    }
}
