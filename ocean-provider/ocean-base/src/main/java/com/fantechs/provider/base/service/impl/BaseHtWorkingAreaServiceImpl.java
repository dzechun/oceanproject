package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkingArea;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtWorkingAreaMapper;
import com.fantechs.provider.base.service.BaseHtWorkingAreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseHtWorkingAreaServiceImpl extends BaseService<BaseHtWorkingArea> implements BaseHtWorkingAreaService {

    @Resource
    private BaseHtWorkingAreaMapper baseHtWorkingAreaMapper;

    @Override
    public List<BaseHtWorkingArea> findList(Map<String, Object> map) {
        return baseHtWorkingAreaMapper.findList(map);
    }
}
