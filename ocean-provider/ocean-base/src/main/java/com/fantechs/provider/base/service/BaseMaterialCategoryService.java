package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseMaterialCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/31.
 */

public interface BaseMaterialCategoryService extends IService<BaseMaterialCategory> {

    List<BaseMaterialCategoryDto> findList(Map<String, Object> map);

}
