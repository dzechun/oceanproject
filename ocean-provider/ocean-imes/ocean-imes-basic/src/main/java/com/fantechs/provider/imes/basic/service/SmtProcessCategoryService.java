package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtProcessCategoryDto;
import com.fantechs.common.base.entity.basic.SmtProcessCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/10/15.
 */

public interface SmtProcessCategoryService extends IService<SmtProcessCategory> {

    List<SmtProcessCategoryDto> findList(Map<String,Object> map);
}
