package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtBadnessCategoryMapper;
import com.fantechs.provider.base.service.BaseHtBadnessCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/02.
 */
@Service
public class BaseHtBadnessCategoryServiceImpl extends BaseService<BaseHtBadnessCategory> implements BaseHtBadnessCategoryService {

    @Resource
    private BaseHtBadnessCategoryMapper baseHtBadnessCategoryMapper;

    @Override
    public List<BaseHtBadnessCategory> findList(Map<String, Object> map) {
        return baseHtBadnessCategoryMapper.findList(map);
    }
}
