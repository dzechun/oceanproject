package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigCategoryDto;
import com.fantechs.common.base.general.entity.eam.EamJigCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamJigCategoryService extends IService<EamJigCategory> {
    List<EamJigCategoryDto> findList(Map<String, Object> map);
}
