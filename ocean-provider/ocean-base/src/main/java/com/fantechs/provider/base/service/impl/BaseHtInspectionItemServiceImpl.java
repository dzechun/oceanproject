package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtInspectionItemMapper;
import com.fantechs.provider.base.service.BaseHtInspectionItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class BaseHtInspectionItemServiceImpl extends BaseService<BaseHtInspectionItem> implements BaseHtInspectionItemService {

    @Resource
    private BaseHtInspectionItemMapper baseHtInspectionItemMapper;


    @Override
    public List<BaseHtInspectionItem> findHtList(Map<String, Object> map) {
        return baseHtInspectionItemMapper.findHtList(map);
    }
}
