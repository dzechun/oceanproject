package com.fantechs.provider.bcm.service;


import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelCategory;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BcmLabelCategoryService extends IService<BcmLabelCategory> {
    List<BcmLabelCategoryDto> findList(SearchBcmLabelCategory searchBcmLabelCategory);
}
