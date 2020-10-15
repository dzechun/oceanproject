package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtProcessCategory;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/10/15.
 */

public interface SmtHtProcessCategoryService extends IService<SmtHtProcessCategory> {

    List<SmtHtProcessCategory> findHtList(Map<String,Object> map);
}
