package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProcessMapper;
import com.fantechs.provider.base.service.BaseHtProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/25.
 */
@Service
public class BaseHtProcessServiceImpl extends BaseService<BaseHtProcess> implements BaseHtProcessService {

    @Resource
    private BaseHtProcessMapper baseHtProcessMapper;

    @Override
    public List<BaseHtProcess> findHtList(SearchBaseProcess searchBaseProcess) {
        return baseHtProcessMapper.findHtList(searchBaseProcess);
    }
}
