package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtShipmentEnterprise;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtShipmentEnterpriseMapper;
import com.fantechs.provider.base.service.BaseHtShipmentEnterpriseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class BaseHtShipmentEnterpriseServiceImpl extends BaseService<BaseHtShipmentEnterprise> implements BaseHtShipmentEnterpriseService {

    @Resource
    private BaseHtShipmentEnterpriseMapper baseHtShipmentEnterpriseMapper;

    @Override
    public List<BaseHtShipmentEnterprise> findHtList(Map<String, Object> map) {
        return baseHtShipmentEnterpriseMapper.findHtList(map);
    }
}
