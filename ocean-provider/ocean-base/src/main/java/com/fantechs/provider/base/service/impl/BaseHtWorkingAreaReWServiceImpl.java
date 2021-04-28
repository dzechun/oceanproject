package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkingAreaReW;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtWorkingAreaReWMapper;
import com.fantechs.provider.base.service.BaseHtWorkingAreaReWService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseHtWorkingAreaReWServiceImpl extends BaseService<BaseHtWorkingAreaReW> implements BaseHtWorkingAreaReWService {

    @Resource
    private BaseHtWorkingAreaReWMapper baseHtWorkingAreaReWMapper;

    @Override
    public List<BaseHtWorkingAreaReW> findList(Map<String, Object> map) {
        return baseHtWorkingAreaReWMapper.findList(map);
    }
}
