package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkshopSection;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtWorkshopSectionMapper;
import com.fantechs.provider.imes.basic.service.SmtHtWorkshopSectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Mr.Lei
 * @create 2020/9/27
 */
@Service
public class SmtSmtHtWorkshopSectionServiceImpl extends BaseService<SmtHtWorkshopSection> implements SmtHtWorkshopSectionService {

    @Resource
    private SmtHtWorkshopSectionMapper smtHtWorkshopSectionMapper;

    @Override
    public List<SmtHtWorkshopSection> findList(SearchSmtWorkshopSection searchSmtWorkshopSection) {
        return smtHtWorkshopSectionMapper.findList(searchSmtWorkshopSection);
    }
}
