package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.electronic.entity.SmtLoading;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.electronic.mapper.SmtHtLoadingMapper;
import com.fantechs.provider.electronic.mapper.SmtLoadingMapper;
import com.fantechs.provider.electronic.service.SmtLoadingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class SmtLoadingServiceImpl extends BaseService<SmtLoading> implements SmtLoadingService {

    @Resource
    private SmtLoadingMapper smtLoadingMapper;

    @Resource
    private SmtHtLoadingMapper smtHtLoadingMapper;

    @Override
    public List<SmtLoading> findList(Map<String, Object> map) {
        return smtLoadingMapper.findList(map);
    }

    @Override
    public List<SmtLoading> findHtList(Map<String, Object> map) {
        return smtHtLoadingMapper.findHtList(map);
    }
}
