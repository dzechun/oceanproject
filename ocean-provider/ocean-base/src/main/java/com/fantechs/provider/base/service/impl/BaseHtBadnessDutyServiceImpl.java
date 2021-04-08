package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessDuty;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtBadnessDutyMapper;
import com.fantechs.provider.base.service.BaseHtBadnessDutyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/08.
 */
@Service
public class BaseHtBadnessDutyServiceImpl extends BaseService<BaseHtBadnessDuty> implements BaseHtBadnessDutyService {

    @Resource
    private BaseHtBadnessDutyMapper baseHtBadnessDutyMapper;

    @Override
    public List<BaseHtBadnessDuty> findList(Map<String, Object> map) {
        return baseHtBadnessDutyMapper.findList(map);
    }
}
