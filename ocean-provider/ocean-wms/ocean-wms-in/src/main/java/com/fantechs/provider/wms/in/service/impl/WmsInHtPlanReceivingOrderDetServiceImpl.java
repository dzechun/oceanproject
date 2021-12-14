package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtPlanReceivingOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInHtPlanReceivingOrderDetMapper;
import com.fantechs.provider.wms.in.service.WmsInHtPlanReceivingOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@Service
public class WmsInHtPlanReceivingOrderDetServiceImpl extends BaseService<WmsInHtPlanReceivingOrderDet> implements WmsInHtPlanReceivingOrderDetService {

    @Resource
    private WmsInHtPlanReceivingOrderDetMapper wmsInHtPlanReceivingOrderDetMapper;

    @Override
    public List<WmsInHtPlanReceivingOrderDetDto> findList(Map<String, Object> map) {
        return wmsInHtPlanReceivingOrderDetMapper.findHtList(map);
    }
}
