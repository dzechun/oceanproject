package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class WmsOutDeliveryOrderDetServiceImpl  extends BaseService<WmsOutDeliveryOrderDet> implements WmsOutDeliveryOrderDetService {

    @Resource
    private WmsOutDeliveryOrderDetMapper wmsOutDeliveryOrderDetMapper;

    @Override
    public List<WmsOutDeliveryOrderDetDto> findList(Map<String, Object> dynamicConditionByEntity) {
        return null;
    }
}
