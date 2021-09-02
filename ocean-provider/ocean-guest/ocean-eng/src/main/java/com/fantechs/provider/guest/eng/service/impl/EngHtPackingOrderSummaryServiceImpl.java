package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummary;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.guest.eng.mapper.EngHtPackingOrderSummaryMapper;
import com.fantechs.provider.guest.eng.service.EngHtPackingOrderSummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class EngHtPackingOrderSummaryServiceImpl extends BaseService<EngHtPackingOrderSummary> implements EngHtPackingOrderSummaryService {

    @Resource
    private EngHtPackingOrderSummaryMapper engHtPackingOrderSummaryMapper;

    @Override
    public List<EngHtPackingOrderSummaryDto> findList(Map<String, Object> map) {
        return engHtPackingOrderSummaryMapper.findList(map);
    }
}
