package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtOrderType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtOrderTypeMapper;
import com.fantechs.provider.base.service.BaseHtOrderTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/26.
 */
@Service
public class BaseHtOrderTypeServiceImpl extends BaseService<BaseHtOrderType> implements BaseHtOrderTypeService {

    @Resource
    private BaseHtOrderTypeMapper baseHtOrderTypeMapper;

    @Override
    public List<BaseHtOrderType> findHtList(Map<String, Object> map) {
        return baseHtOrderTypeMapper.findHtList(map);
    }
}
