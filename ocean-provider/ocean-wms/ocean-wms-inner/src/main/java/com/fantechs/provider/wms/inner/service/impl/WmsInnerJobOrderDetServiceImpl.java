package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */
@Service
public class WmsInnerJobOrderDetServiceImpl extends BaseService<WmsInnerJobOrderDet> implements WmsInnerJobOrderDetService {

    @Resource
    private WmsInnerJobOrderDetMapper wmsInPutawayOrderDetMapper;

    @Override
    public List<WmsInnerJobOrderDetDto> findList(Map<String, Object> map) {
        return wmsInPutawayOrderDetMapper.findList(map);
    }
}
