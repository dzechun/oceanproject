package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummaryDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.guest.eng.mapper.EngHtPackingOrderSummaryDetMapper;
import com.fantechs.provider.guest.eng.service.EngHtPackingOrderSummaryDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class EngHtPackingOrderSummaryDetServiceImpl extends BaseService<EngHtPackingOrderSummaryDet> implements EngHtPackingOrderSummaryDetService {

    @Resource
    private EngHtPackingOrderSummaryDetMapper engHtPackingOrderSummaryDetMapper;

    @Override
    public List<EngHtPackingOrderSummaryDetDto> findList(Map<String, Object> map) {
        return engHtPackingOrderSummaryDetMapper.findList(map);
    }
}
