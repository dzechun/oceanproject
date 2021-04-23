package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAcReDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlanAcRe;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseSamplePlanAcReMapper;
import com.fantechs.provider.base.service.BaseSamplePlanAcReService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class BaseSamplePlanAcReServiceImpl extends BaseService<BaseSamplePlanAcRe> implements BaseSamplePlanAcReService {

    @Resource
    private BaseSamplePlanAcReMapper baseSamplePlanAcReMapper;

    @Override
    public List<BaseSamplePlanAcReDto> findList(Map<String, Object> map) {
        return baseSamplePlanAcReMapper.findList(map);
    }
}
