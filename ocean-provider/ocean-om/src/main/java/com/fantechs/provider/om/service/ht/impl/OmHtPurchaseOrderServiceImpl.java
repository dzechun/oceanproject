package com.fantechs.provider.om.service.ht.impl;

import com.fantechs.common.base.general.dto.om.OmHtPurchaseOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseOrderMapper;
import com.fantechs.provider.om.service.ht.OmHtPurchaseOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/20.
 */
@Service
public class OmHtPurchaseOrderServiceImpl extends BaseService<OmHtPurchaseOrder> implements OmHtPurchaseOrderService {

    @Resource
    private OmHtPurchaseOrderMapper omHtPurchaseOrderMapper;

    @Override
    public List<OmHtPurchaseOrderDto> findList(Map<String, Object> map) {
        return omHtPurchaseOrderMapper.findList(map);
    }
}
