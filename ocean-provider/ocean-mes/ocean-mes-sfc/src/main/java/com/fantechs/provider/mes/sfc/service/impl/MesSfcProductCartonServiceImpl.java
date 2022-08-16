package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductCartonMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcProductCartonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/08.
 */
@Service
public class MesSfcProductCartonServiceImpl extends BaseService<MesSfcProductCarton> implements MesSfcProductCartonService {

    @Resource
    private MesSfcProductCartonMapper mesSfcProductCartonMapper;

    @Override
    public List<MesSfcProductCartonDto> findList(Map<String, Object> map) {
        return mesSfcProductCartonMapper.findList(map);
    }
}
