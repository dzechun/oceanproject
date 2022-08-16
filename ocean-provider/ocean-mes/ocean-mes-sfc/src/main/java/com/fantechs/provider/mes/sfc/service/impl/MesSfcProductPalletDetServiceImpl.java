package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPalletDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductPalletDetMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/08.
 */
@Service
public class MesSfcProductPalletDetServiceImpl extends BaseService<MesSfcProductPalletDet> implements MesSfcProductPalletDetService {

    @Resource
    private MesSfcProductPalletDetMapper mesSfcProductPalletDetMapper;

    @Override
    public List<MesSfcProductPalletDetDto> findList(Map<String, Object> map) {
        return mesSfcProductPalletDetMapper.findList(map);
    }

    @Override
    public List<PalletAutoAsnDto> findListGroupByWorkOrder(Map<String, Object> map) {
        return mesSfcProductPalletDetMapper.findListGroupByWorkOrder(map);
    }
}
