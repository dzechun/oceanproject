package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionStandard;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtInspectionStandardMapper;
import com.fantechs.provider.base.service.BaseHtInspectionStandardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */
@Service
public class BaseHtInspectionStandardServiceImpl extends BaseService<BaseHtInspectionStandard> implements BaseHtInspectionStandardService {

    @Resource
    private BaseHtInspectionStandardMapper baseHtInspectionStandardMapper;

    @Override
    public List<BaseHtInspectionStandard> findHtList(Map<String, Object> map) {
        return baseHtInspectionStandardMapper.findHtList(map);
    }
}
