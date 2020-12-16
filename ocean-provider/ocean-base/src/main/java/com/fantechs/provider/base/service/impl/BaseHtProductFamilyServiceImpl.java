package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProductFamilyMapper;
import com.fantechs.provider.base.service.BaseHtProductFamilyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/15.
 */
@Service
public class BaseHtProductFamilyServiceImpl extends BaseService<BaseHtProductFamily> implements BaseHtProductFamilyService {

    @Resource
    private BaseHtProductFamilyMapper baseHtProductFamilyMapper;

    @Override
    public List<BaseHtProductFamily> findHtList(Map<String, Object> map) {
        return baseHtProductFamilyMapper.findHtList(map);
    }
}
