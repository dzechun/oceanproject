package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@Service
public class WmsInnerInventoryServiceImpl extends BaseService<WmsInnerInventory> implements WmsInnerInventoryService {

    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Override
    public List<WmsInnerInventoryDto> findList(Map<String, Object> map) {
        return wmsInnerInventoryMapper.findList(map);

    }
}
