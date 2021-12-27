package com.fantechs.provider.om.service.ht.impl;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseOrderDetMapper;
import com.fantechs.provider.om.service.ht.OmHtPurchaseOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/20.
 */
@Service
public class OmHtPurchaseOrderDetServiceImpl extends BaseService<OmHtPurchaseOrderDet> implements OmHtPurchaseOrderDetService {

    @Resource
    private OmHtPurchaseOrderDetMapper omHtPurchaseOrderDetMapper;

    @Override
    public List<OmPurchaseOrderDetDto> findList(Map<String, Object> map) {
        return omHtPurchaseOrderDetMapper.findList(map);
    }
}
