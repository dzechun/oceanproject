package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcCartonPalletReprintDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcCartonPalletReprint;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcCartonPalletReprintMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcCartonPalletReprintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */
@Service
public class MesSfcCartonPalletReprintServiceImpl extends BaseService<MesSfcCartonPalletReprint> implements MesSfcCartonPalletReprintService {

    @Resource
    private MesSfcCartonPalletReprintMapper mesSfcCartonPalletReprintMapper;

    @Override
    public List<MesSfcCartonPalletReprintDto> findList(Map<String, Object> map) {
        return mesSfcCartonPalletReprintMapper.findList(map);
    }
}
