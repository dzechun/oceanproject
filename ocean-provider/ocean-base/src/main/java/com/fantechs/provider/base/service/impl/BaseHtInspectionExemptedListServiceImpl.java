package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionExemptedList;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtInspectionExemptedListMapper;
import com.fantechs.provider.base.service.BaseHtInspectionExemptedListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/21.
 */
@Service
public class BaseHtInspectionExemptedListServiceImpl extends BaseService<BaseHtInspectionExemptedList> implements BaseHtInspectionExemptedListService {

    @Resource
    private BaseHtInspectionExemptedListMapper baseHtInspectionExemptedListMapper;

    @Override
    public List<BaseHtInspectionExemptedList> findHtList(Map<String, Object> map) {
        return baseHtInspectionExemptedListMapper.findHtList(map);
    }
}
