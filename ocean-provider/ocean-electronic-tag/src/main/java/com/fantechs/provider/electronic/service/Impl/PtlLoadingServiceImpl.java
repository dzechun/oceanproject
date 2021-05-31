package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.electronic.entity.PtlLoading;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.electronic.mapper.PtlHtLoadingMapper;
import com.fantechs.provider.electronic.mapper.PtlLoadingMapper;
import com.fantechs.provider.electronic.service.PtlLoadingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class PtlLoadingServiceImpl extends BaseService<PtlLoading> implements PtlLoadingService {

    @Resource
    private PtlLoadingMapper ptlLoadingMapper;

    @Resource
    private PtlHtLoadingMapper ptlHtLoadingMapper;

    @Override
    public List<PtlLoading> findList(Map<String, Object> map) {
        return ptlLoadingMapper.findList(map);
    }

    @Override
    public List<PtlLoading> findHtList(Map<String, Object> map) {
        return ptlHtLoadingMapper.findHtList(map);
    }
}
