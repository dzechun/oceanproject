package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummary;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtPackingOrderSummaryMapper;
import com.fantechs.provider.srm.service.SrmHtPackingOrderSummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class SrmHtPackingOrderSummaryServiceImpl extends BaseService<SrmHtPackingOrderSummary> implements SrmHtPackingOrderSummaryService {

    @Resource
    private SrmHtPackingOrderSummaryMapper srmHtPackingOrderSummaryMapper;

    @Override
    public List<SrmHtPackingOrderSummaryDto> findList(Map<String, Object> map) {
        return srmHtPackingOrderSummaryMapper.findList(map);
    }
}
