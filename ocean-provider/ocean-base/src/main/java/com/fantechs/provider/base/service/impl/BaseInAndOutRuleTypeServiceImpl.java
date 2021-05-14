package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseInAndOutRuleTypeMapper;
import com.fantechs.provider.base.service.BaseInAndOutRuleTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/14.
 */
@Service
public class BaseInAndOutRuleTypeServiceImpl extends BaseService<BaseInAndOutRuleType> implements BaseInAndOutRuleTypeService {

    @Resource
    private BaseInAndOutRuleTypeMapper baseInAndOutRuleTypeMapper;

    @Override
    public List<BaseInAndOutRuleType> findList(Map<String, Object> map) {
        return baseInAndOutRuleTypeMapper.findList(map);
    }
}
