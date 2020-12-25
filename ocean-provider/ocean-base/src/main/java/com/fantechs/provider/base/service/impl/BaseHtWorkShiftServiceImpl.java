package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShift;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtWorkShiftMapper;
import com.fantechs.provider.base.service.BaseHtWorkShiftService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/21.
 */
@Service
public class BaseHtWorkShiftServiceImpl extends BaseService<BaseHtWorkShift> implements BaseHtWorkShiftService {

    @Resource
    private BaseHtWorkShiftMapper baseHtWorkShiftMapper;

    @Override
    public List<BaseHtWorkShift> findHtList(Map<String, Object> map) {
        return baseHtWorkShiftMapper.findHtList(map);
    }
}
