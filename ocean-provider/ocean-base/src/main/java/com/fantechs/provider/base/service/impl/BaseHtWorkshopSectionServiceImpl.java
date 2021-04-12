package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkshopSection;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtWorkshopSectionMapper;
import com.fantechs.provider.base.service.BaseHtWorkshopSectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Mr.Lei
 * @create 2020/9/27
 */
@Service
public class BaseHtWorkshopSectionServiceImpl extends BaseService<BaseHtWorkshopSection> implements BaseHtWorkshopSectionService {

    @Resource
    private BaseHtWorkshopSectionMapper baseHtWorkshopSectionMapper;

    @Override
    public List<BaseHtWorkshopSection> findList(SearchBaseWorkshopSection searchBaseWorkshopSection) {
        return baseHtWorkshopSectionMapper.findList(searchBaseWorkshopSection);
    }
}
