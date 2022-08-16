package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@Service
public class WmsOutDeliveryOrderDetServiceImpl extends BaseService<WmsOutDeliveryOrderDet> implements WmsOutDeliveryOrderDetService {

    @Resource
    private WmsOutDeliveryOrderDetMapper wmsOutDeliveryOrderDetMapper;

    @Override
    public List<WmsOutDeliveryOrderDetDto> findList(Map<String, Object> map) {
        return wmsOutDeliveryOrderDetMapper.findList(map);
    }

    @Override
    public int update(WmsOutDeliveryOrderDet entity) {
        WmsOutDeliveryOrderDet wms = wmsOutDeliveryOrderDetMapper.selectByPrimaryKey(entity.getDeliveryOrderDetId());
        if(StringUtils.isEmpty(wms.getPickingQty())){
            wms.setPickingQty(BigDecimal.ZERO);
        }
        wms.setPickingQty(entity.getPickingQty());
        return super.update(wms);
    }
}
