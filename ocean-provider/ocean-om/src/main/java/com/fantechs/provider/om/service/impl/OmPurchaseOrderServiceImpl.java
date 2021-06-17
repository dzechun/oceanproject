package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.OmPurchaseOrderMapper;
import com.fantechs.provider.om.service.OmPurchaseOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@Service
public class OmPurchaseOrderServiceImpl extends BaseService<OmPurchaseOrder> implements OmPurchaseOrderService {

    @Resource
    private OmPurchaseOrderMapper omPurchaseOrderMapper;

    /*@Override
    public List<OmPurchaseOrderDto> findList(Map<String, Object> map) {
        return omPurchaseOrderMapper.findList(map);
    }*/
}
