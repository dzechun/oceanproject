package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInReceivingOrderDetMapper;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@Service
public class WmsInReceivingOrderDetServiceImpl extends BaseService<WmsInReceivingOrderDet> implements WmsInReceivingOrderDetService {

    @Resource
    private WmsInReceivingOrderDetMapper wmsInReceivingOrderDetMapper;

    @Override
    public List<WmsInReceivingOrderDetDto> findList(Map<String, Object> map) {
        return wmsInReceivingOrderDetMapper.findList(map);
    }
}
