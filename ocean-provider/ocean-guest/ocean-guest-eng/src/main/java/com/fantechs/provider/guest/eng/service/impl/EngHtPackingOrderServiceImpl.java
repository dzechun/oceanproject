package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.guest.eng.mapper.EngHtPackingOrderMapper;
import com.fantechs.provider.guest.eng.service.EngHtPackingOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class EngHtPackingOrderServiceImpl extends BaseService<EngHtPackingOrder> implements EngHtPackingOrderService {

    @Resource
    private EngHtPackingOrderMapper engHtPackingOrderMapper;

    @Override
    public List<EngHtPackingOrderDto> findList(Map<String, Object> map) {
        return engHtPackingOrderMapper.findList(map);
    }
}
