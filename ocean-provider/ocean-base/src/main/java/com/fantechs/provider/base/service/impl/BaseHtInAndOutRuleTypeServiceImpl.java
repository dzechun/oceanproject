package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRuleType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtInAndOutRuleTypeMapper;
import com.fantechs.provider.base.service.BaseHtInAndOutRuleTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/14.
 */
@Service
public class BaseHtInAndOutRuleTypeServiceImpl extends BaseService<BaseHtInAndOutRuleType> implements BaseHtInAndOutRuleTypeService {

    @Resource
    private BaseHtInAndOutRuleTypeMapper baseHtInAndOutRuleTypeMapper;

    @Override
    public List<BaseHtInAndOutRuleType> findHtList(Map<String, Object> map) {
        return baseHtInAndOutRuleTypeMapper.findHtList(map);
    }
}
