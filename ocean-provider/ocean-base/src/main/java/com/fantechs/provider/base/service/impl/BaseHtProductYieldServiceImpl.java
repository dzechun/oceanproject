package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductYield;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProductYieldMapper;
import com.fantechs.provider.base.service.BaseHtProductYieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/20.
 */
@Service
public class BaseHtProductYieldServiceImpl extends BaseService<BaseHtProductYield> implements BaseHtProductYieldService {

    @Resource
    private BaseHtProductYieldMapper baseHtProductYieldMapper;

    @Override
    public List<BaseHtProductYield> findHtList(Map<String, Object> map) {
        return baseHtProductYieldMapper.findList(map);
    }

}
