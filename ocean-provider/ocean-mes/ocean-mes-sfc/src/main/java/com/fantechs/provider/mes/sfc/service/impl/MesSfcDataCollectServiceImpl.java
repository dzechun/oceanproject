package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcDataCollectDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcDataCollect;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcDataCollectMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcDataCollectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/19.
 */
@Service
public class MesSfcDataCollectServiceImpl extends BaseService<MesSfcDataCollect> implements MesSfcDataCollectService {

    @Resource
    private MesSfcDataCollectMapper mesSfcDataCollectMapper;

    @Override
    public List<MesSfcDataCollectDto> findList(Map<String, Object> map) {
        return mesSfcDataCollectMapper.findList(map);
    }

    @Override
    public List<MesSfcDataCollectDto> findByGroup() {
        return mesSfcDataCollectMapper.findByGroup();
    }
}
