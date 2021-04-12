package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProcessCategoryMapper;
import com.fantechs.provider.base.service.BaseHtProcessCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/10/15.
 */
@Service
public class BaseHtProcessCategoryServiceImpl extends BaseService<BaseHtProcessCategory> implements BaseHtProcessCategoryService {

    @Resource
    private BaseHtProcessCategoryMapper baseHtProcessCategoryMapper;

    @Override
    public List<BaseHtProcessCategory> findHtList(Map<String, Object> map) {
        return baseHtProcessCategoryMapper.findHtList(map);
    }
}
