package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtReceivingOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInHtReceivingOrderDetMapper;
import com.fantechs.provider.wms.in.service.WmsInHtReceivingOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/15.
 */
@Service
public class WmsInHtReceivingOrderDetServiceImpl extends BaseService<WmsInHtReceivingOrderDet> implements WmsInHtReceivingOrderDetService {

    @Resource
    private WmsInHtReceivingOrderDetMapper wmsInHtReceivingOrderDetMapper;

    @Override
    public List<WmsInHtReceivingOrderDetDto> findList(Map<String, Object> map) {
        return wmsInHtReceivingOrderDetMapper.findHtList(map);
    }
}
