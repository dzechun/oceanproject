package com.fantechs.provider.bcm.service.impl;

import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelCategory;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.bcm.mapper.BcmHtLabelCategoryMapper;
import com.fantechs.provider.bcm.service.BcmHtLabelCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BcmHtLabelCategoryServiceImpl  extends BaseService<BcmHtLabelCategory> implements BcmHtLabelCategoryService {

         @Resource
         private BcmHtLabelCategoryMapper bcmHtLabelCategoryMapper;

    @Override
    public List<BcmLabelCategoryDto> findList(SearchBcmLabelCategory searchBcmLabelCategory) {
        return bcmHtLabelCategoryMapper.findList(searchBcmLabelCategory);
    }
}
