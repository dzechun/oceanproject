package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtPackingOrderMapper;
import com.fantechs.provider.srm.service.SrmHtPackingOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class SrmHtPackingOrderServiceImpl extends BaseService<SrmHtPackingOrder> implements SrmHtPackingOrderService {

    @Resource
    private SrmHtPackingOrderMapper srmHtPackingOrderMapper;

    @Override
    public List<SrmHtPackingOrderDto> findList(Map<String, Object> map) {
        return srmHtPackingOrderMapper.findList(map);
    }
}
