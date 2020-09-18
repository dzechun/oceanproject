package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtFactoryMapper;
import com.fantechs.provider.imes.basic.service.SmtHtFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
@Service
@Slf4j
public class SmtHtFactoryServiceImpl extends BaseService<SmtHtFactory>  implements SmtHtFactoryService {
    @Autowired
    private SmtHtFactoryMapper smtHtFactoryMapper;
    @Override
    public List<SmtHtFactory> findList(Map<String, Object> map) {
        return smtHtFactoryMapper.findList(map);
    }
}
