package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWarning;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtWarningMapper;
import com.fantechs.provider.base.service.BaseHtWarningService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/03.
 */
@Service
public class BaseHtWarningServiceImpl extends BaseService<BaseHtWarning> implements BaseHtWarningService {

    @Resource
    private BaseHtWarningMapper baseHtWarningMapper;

    @Override
    public List<BaseHtWarning> findHtList(Map<String, Object> map) {
        return baseHtWarningMapper.findHtList(map);
    }
}
