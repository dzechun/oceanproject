package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseDept;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtDeptMapper;
import com.fantechs.provider.base.service.BaseHtDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BaseHtDeptServiceImpl extends BaseService<BaseHtDept> implements BaseHtDeptService {

    @Resource
    private BaseHtDeptMapper baseHtDeptMapper;

    @Override
    public List<BaseHtDept> selectHtDepts(SearchBaseDept searchBaseDept) {
        return baseHtDeptMapper.selectHtDepts(searchBaseDept);
    }
}
