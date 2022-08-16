package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBadnessCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/02.
 */

public interface BaseBadnessCategoryService extends IService<BaseBadnessCategory> {
    List<BaseBadnessCategoryDto> findList(Map<String, Object> map);

    BaseBadnessCategory saveByApi (BaseBadnessCategory baseBadnessCategory);
}
