package com.fantechs.provider.bcm.service;


import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelCategory;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BcmHtLabelCategoryService extends IService<BcmHtLabelCategory> {
    List<BcmHtLabelCategory> findList(SearchBcmLabelCategory searchBcmLabelCategory);
}
