package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInHtInPlanOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInHtInPlanOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class WmsInHtInPlanOrderServiceImpl extends BaseService<WmsInHtInPlanOrder> implements WmsInHtInPlanOrderService {

    @Resource
    private WmsInHtInPlanOrderMapper wmsInHtInPlanOrderMapper;

    @Override
    public List<WmsInInPlanOrderDto> findList(Map<String, Object> map) {
     //   return wmsInHtInPlanOrderMapper.findList(map);
        return null;
    }

}
