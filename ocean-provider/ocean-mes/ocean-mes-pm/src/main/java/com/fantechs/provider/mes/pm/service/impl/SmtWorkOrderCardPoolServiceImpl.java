package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardPool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
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
