package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.WorkshopSection;
import com.fantechs.common.base.entity.basic.history.HtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchWorkshopSection;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.HtWorkshopSectionMapper;
import com.fantechs.provider.imes.basic.service.HtWorkshopSectionService;
import com.fantechs.provider.imes.basic.service.WorkshopSectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Mr.Lei
 * @create 2020/9/27
 */
@Service
public class HtWorkshopSectionServiceImpl extends BaseService<HtWorkshopSection> implements HtWorkshopSectionService {

    @Resource
    private HtWorkshopSectionMapper htWorkshopSectionMapper;

    @Override
    public List<HtWorkshopSection> findHtList(SearchWorkshopSection searchWorkshopSection) {
        return htWorkshopSectionMapper.selectHtSection(searchWorkshopSection);
    }
}
