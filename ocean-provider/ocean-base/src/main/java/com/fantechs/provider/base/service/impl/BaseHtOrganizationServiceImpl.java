package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtOrganization;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtOrganizationMapper;
import com.fantechs.provider.base.service.BaseHtOrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/29.
 */
@Service
public class BaseHtOrganizationServiceImpl extends BaseService<BaseHtOrganization> implements BaseHtOrganizationService {

    @Resource
    private BaseHtOrganizationMapper baseHtOrganizationMapper;

    @Override
    public List<BaseHtOrganization> findHtList(Map<String, Object> map) {
        return baseHtOrganizationMapper.findHtList(map);
    }
}
