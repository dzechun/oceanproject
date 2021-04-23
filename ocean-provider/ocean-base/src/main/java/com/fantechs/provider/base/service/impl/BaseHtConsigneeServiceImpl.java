package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtConsignee;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtConsigneeMapper;
import com.fantechs.provider.base.service.BaseHtConsigneeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/23.
 */
@Service
public class BaseHtConsigneeServiceImpl extends BaseService<BaseHtConsignee> implements BaseHtConsigneeService {

    @Resource
    private BaseHtConsigneeMapper baseHtConsigneeMapper;

    @Override
    public List<BaseHtConsignee> findHtList(Map<String, Object> map) {
        return baseHtConsigneeMapper.findHtList(map);
    }
}
