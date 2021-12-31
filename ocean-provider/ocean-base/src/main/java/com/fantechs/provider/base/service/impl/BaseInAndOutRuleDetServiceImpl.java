package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseHtInAndOutRuleDetDto;
import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtInAndOutRuleDetMapper;
import com.fantechs.provider.base.mapper.BaseInAndOutRuleDetMapper;
import com.fantechs.provider.base.service.BaseInAndOutRuleDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/30.
 */
@Service
public class BaseInAndOutRuleDetServiceImpl extends BaseService<BaseInAndOutRuleDet> implements BaseInAndOutRuleDetService {

    @Resource
    private BaseInAndOutRuleDetMapper baseInAndOutRuleDetMapper;
    @Resource
    private BaseHtInAndOutRuleDetMapper baseHtInAndOutRuleDetMapper;

    @Override
    public List<BaseInAndOutRuleDetDto> findList(Map<String, Object> map) {
        return baseInAndOutRuleDetMapper.findList(map);
    }

    @Override
    public List<BaseHtInAndOutRuleDetDto> findHtList(Map<String, Object> map) {
        return baseHtInAndOutRuleDetMapper.findList(map);
    }
}
