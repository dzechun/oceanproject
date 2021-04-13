package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.BaseHtBadnessCause;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtBadnessCauseMapper;
import com.fantechs.provider.base.service.BaseHtBadnessCauseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */
@Service
public class BaseHtBadnessCauseServiceImpl extends BaseService<BaseHtBadnessCause> implements BaseHtBadnessCauseService {

    @Resource
    private BaseHtBadnessCauseMapper baseHtBadnessCauseMapper;

    @Override
    public List<BaseHtBadnessCause> findHtList(Map<String, Object> map) {
        return baseHtBadnessCauseMapper.findHtList(map);
    }
}
