package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtMaterialCategoryDto;
import com.fantechs.common.base.entity.basic.SmtMaterialCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/31.
 */

public interface SmtMaterialCategoryService extends IService<SmtMaterialCategory> {

    List<SmtMaterialCategoryDto> findList(Map<String, Object> map);

}
