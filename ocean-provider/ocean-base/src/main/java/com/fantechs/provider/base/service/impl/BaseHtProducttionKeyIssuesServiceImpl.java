package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProducttionKeyIssues;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProducttionKeyIssuesMapper;
import com.fantechs.provider.base.service.BaseHtProducttionKeyIssuesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/10.
 */
@Service
public class BaseHtProducttionKeyIssuesServiceImpl extends BaseService<BaseHtProducttionKeyIssues> implements BaseHtProducttionKeyIssuesService {

    @Resource
    private BaseHtProducttionKeyIssuesMapper baseHtProducttionKeyIssuesMapper;

    @Override
    public List<BaseHtProducttionKeyIssues> findHtList(Map<String, Object> map) {
        return baseHtProducttionKeyIssuesMapper.findHtList(map);
    }
}
