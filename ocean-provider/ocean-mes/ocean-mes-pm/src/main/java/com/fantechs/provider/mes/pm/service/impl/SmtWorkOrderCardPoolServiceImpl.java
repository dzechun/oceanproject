package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.dto.apply.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardPool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.mapper.SmtWorkOrderCardPoolMapper;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmtWorkOrderCardPoolServiceImpl extends BaseService<SmtWorkOrderCardPool> implements SmtWorkOrderCardPoolService {

    @Resource
    private SmtWorkOrderCardPoolMapper smtWorkOrderCardPoolMapper;
    @Override
    public List<SmtWorkOrderCardPoolDto> findList(SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool) {
        return smtWorkOrderCardPoolMapper.findList(searchSmtWorkOrderCardPool);
    }
}
