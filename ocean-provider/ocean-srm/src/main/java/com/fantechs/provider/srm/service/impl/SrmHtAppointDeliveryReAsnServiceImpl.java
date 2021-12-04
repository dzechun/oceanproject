package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.dto.srm.SrmHtAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtAppointDeliveryReAsn;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtAppointDeliveryReAsnMapper;
import com.fantechs.provider.srm.service.SrmHtAppointDeliveryReAsnService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */
@Service
public class SrmHtAppointDeliveryReAsnServiceImpl extends BaseService<SrmHtAppointDeliveryReAsn> implements SrmHtAppointDeliveryReAsnService {

    @Resource
    private SrmHtAppointDeliveryReAsnMapper srmHtAppointDeliveryReAsnMapper;

    @Override
    public List<SrmHtAppointDeliveryReAsnDto> findList(Map<String, Object> map) {
        return srmHtAppointDeliveryReAsnMapper.findList(map);
    }

}
