package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BaseLabelCategoryService extends IService<BaseLabelCategory> {
    List<BaseLabelCategoryDto> findList(SearchBaseLabelCategory searchBaseLabelCategory);
}
