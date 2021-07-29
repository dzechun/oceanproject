package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.general.dto.eam.EamDataCollectDto;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.eam.mapper.EamDataCollectMapper;
import com.fantechs.provider.eam.service.EamDataCollectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/19.
 */
@Service
public class EamDataCollectServiceImpl extends BaseService<EamDataCollect> implements EamDataCollectService {

    @Resource
    private EamDataCollectMapper eamDataCollectMapper;

    @Override
    public List<EamDataCollectDto> findList(Map<String, Object> map) {
        return eamDataCollectMapper.findList(map);
    }

    @Override
    public List<EamDataCollectDto> findByGroup(Long equipmentId) {
        return eamDataCollectMapper.findByGroup(equipmentId);
    }
}
