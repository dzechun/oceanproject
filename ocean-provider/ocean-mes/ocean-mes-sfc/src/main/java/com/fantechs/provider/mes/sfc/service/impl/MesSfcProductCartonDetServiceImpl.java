package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDetDto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCartonDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductCartonDetMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcProductCartonDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/08.
 */
@Service
public class MesSfcProductCartonDetServiceImpl extends BaseService<MesSfcProductCartonDet> implements MesSfcProductCartonDetService {

    @Resource
    private MesSfcProductCartonDetMapper mesSfcProductCartonDetMapper;

    @Override
    public List<MesSfcProductCartonDetDto> findList(Map<String, Object> map) {
        return mesSfcProductCartonDetMapper.findList(map);
    }

    @Override
    public List<MesSfcProductCartonDetDto> findRelationList(Map<String, Object> map) {
        return mesSfcProductCartonDetMapper.findRelationList(map);
    }

    @Override
    public List<PalletAutoAsnDto> findListGroupByWorkOrder(Map<String, Object> map) {
        return mesSfcProductCartonDetMapper.findListGroupByWorkOrder(map);
    }
}
