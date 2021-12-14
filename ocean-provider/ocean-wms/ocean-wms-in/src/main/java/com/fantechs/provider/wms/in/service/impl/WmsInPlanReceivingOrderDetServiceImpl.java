package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInPlanReceivingOrderDetMapper;
import com.fantechs.provider.wms.in.service.WmsInPlanReceivingOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@Service
public class WmsInPlanReceivingOrderDetServiceImpl extends BaseService<WmsInPlanReceivingOrderDet> implements WmsInPlanReceivingOrderDetService {

    @Resource
    private WmsInPlanReceivingOrderDetMapper wmsInPlanReceivingOrderDetMapper;

    @Override
    public List<WmsInPlanReceivingOrderDetDto> findList(Map<String, Object> map) {
        return wmsInPlanReceivingOrderDetMapper.findList(map);
    }
}
