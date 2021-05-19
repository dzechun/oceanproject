package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleProcess;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtSampleProcessMapper;
import com.fantechs.provider.base.service.BaseHtSampleProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */
@Service
public class BaseHtSampleProcessServiceImpl extends BaseService<BaseHtSampleProcess> implements BaseHtSampleProcessService {

    @Resource
    private BaseHtSampleProcessMapper baseHtSampleProcessMapper;

    @Override
    public List<BaseHtSampleProcess> findHtList(Map<String, Object> map) {
        return baseHtSampleProcessMapper.findHtList(map);
    }
}
