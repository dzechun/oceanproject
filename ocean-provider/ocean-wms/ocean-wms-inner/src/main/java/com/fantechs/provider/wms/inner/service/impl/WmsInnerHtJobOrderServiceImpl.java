package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerHtJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtJobOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtJobOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerHtJobOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/01.
 */
@Service
public class WmsInnerHtJobOrderServiceImpl extends BaseService<WmsInnerHtJobOrder> implements WmsInnerHtJobOrderService {

    @Resource
    private WmsInnerHtJobOrderMapper wmsInnerHtJobOrderMapper;

    @Override
    public List<WmsInnerHtJobOrderDto> findList(Map<String, Object> map) {
        return wmsInnerHtJobOrderMapper.findList(map);
    }
}
