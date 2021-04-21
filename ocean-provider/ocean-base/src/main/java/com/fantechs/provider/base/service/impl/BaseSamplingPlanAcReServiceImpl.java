package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanAcReDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlanAcRe;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseSamplingPlanAcReMapper;
import com.fantechs.provider.base.service.BaseSamplingPlanAcReService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class BaseSamplingPlanAcReServiceImpl extends BaseService<BaseSamplingPlanAcRe> implements BaseSamplingPlanAcReService {

    @Resource
    private BaseSamplingPlanAcReMapper baseSamplingPlanAcReMapper;

    @Override
    public List<BaseSamplingPlanAcReDto> findList(Map<String, Object> map) {
        return baseSamplingPlanAcReMapper.findList(map);
    }
}
