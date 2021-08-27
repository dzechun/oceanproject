package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummaryDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtPackingOrderSummaryDetMapper;
import com.fantechs.provider.srm.service.SrmHtPackingOrderSummaryDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class SrmHtPackingOrderSummaryDetServiceImpl extends BaseService<SrmHtPackingOrderSummaryDet> implements SrmHtPackingOrderSummaryDetService {

    @Resource
    private SrmHtPackingOrderSummaryDetMapper srmHtPackingOrderSummaryDetMapper;

    @Override
    public List<SrmHtPackingOrderSummaryDetDto> findList(Map<String, Object> map) {
        return srmHtPackingOrderSummaryDetMapper.findList(map);
    }
}
