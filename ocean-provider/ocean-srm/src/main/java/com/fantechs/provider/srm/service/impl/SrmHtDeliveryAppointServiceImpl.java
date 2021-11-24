package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryAppoint;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtDeliveryAppointMapper;
import com.fantechs.provider.srm.service.SrmHtDeliveryAppointService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */
@Service
public class SrmHtDeliveryAppointServiceImpl extends BaseService<SrmHtDeliveryAppoint> implements SrmHtDeliveryAppointService {

    @Resource
    private SrmHtDeliveryAppointMapper srmHtDeliveryAppointMapper;

    @Override
    public List<SrmHtDeliveryAppoint> findList(Map<String, Object> map) {
        return srmHtDeliveryAppointMapper.findList(map);
    }
}
