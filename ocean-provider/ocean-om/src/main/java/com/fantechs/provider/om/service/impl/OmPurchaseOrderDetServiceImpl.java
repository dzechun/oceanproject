package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.OmPurchaseOrderDetMapper;
import com.fantechs.provider.om.service.OmPurchaseOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@Service
public class OmPurchaseOrderDetServiceImpl extends BaseService<OmPurchaseOrderDet> implements OmPurchaseOrderDetService {

    @Resource
    private OmPurchaseOrderDetMapper omPurchaseOrderDetMapper;

   /* @Override
    public List<OmPurchaseOrderDetDto> findList(Map<String, Object> map) {
        return omPurchaseOrderDetMapper.findList(map);
    }*/
}
