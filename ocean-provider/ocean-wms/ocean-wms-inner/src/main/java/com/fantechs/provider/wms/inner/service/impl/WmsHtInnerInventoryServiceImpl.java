package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.entity.wms.inner.history.WmsHtInnerInventory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsHtInnerInventoryMapper;
import com.fantechs.provider.wms.inner.service.WmsHtInnerInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@Service
public class WmsHtInnerInventoryServiceImpl extends BaseService<WmsHtInnerInventory> implements WmsHtInnerInventoryService {

    @Resource
    private WmsHtInnerInventoryMapper wmsHtInnerInventoryMapper;

    @Override
    public List<WmsHtInnerInventory> findList(Map<String, Object> map) {
        return wmsHtInnerInventoryMapper.findList(map);
    }
}
