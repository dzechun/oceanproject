package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcKeyPartRelevanceDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcKeyPartRelevance;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcKeyPartRelevanceMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcKeyPartRelevanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/08.
 */
@Service
public class MesSfcKeyPartRelevanceServiceImpl extends BaseService<MesSfcKeyPartRelevance> implements MesSfcKeyPartRelevanceService {

    @Resource
    private MesSfcKeyPartRelevanceMapper mesSfcKeyPartRelevanceMapper;

    @Override
    public List<MesSfcKeyPartRelevanceDto> findList(Map<String, Object> map) {
        return mesSfcKeyPartRelevanceMapper.findList(map);
    }

    @Override
    public List<MesSfcKeyPartRelevanceDto> findListByPallet(Map<String, Object> map) {
        return mesSfcKeyPartRelevanceMapper.findListByPallet(map);
    }

    @Override
    public List<MesSfcKeyPartRelevanceDto> findListForGroup(Map<String, Object> map) {
        return mesSfcKeyPartRelevanceMapper.findListForGroup(map);
    }
}
