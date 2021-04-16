package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtInspectionTypeMapper;
import com.fantechs.provider.base.service.BaseHtInspectionTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
@Service
public class BaseHtInspectionTypeServiceImpl extends BaseService<BaseHtInspectionType> implements BaseHtInspectionTypeService {

    @Resource
    private BaseHtInspectionTypeMapper baseHtInspectionTypeMapper;


    @Override
    public List<BaseHtInspectionType> findHtList(Map<String, Object> map) {
        return baseHtInspectionTypeMapper.findHtList(map);
    }
}
