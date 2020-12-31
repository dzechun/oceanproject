package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.SmtHtMaterialCategory;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/31.
 */

public interface SmtHtMaterialCategoryService extends IService<SmtHtMaterialCategory> {

    List<SmtHtMaterialCategory> findHtList(Map<String, Object> map);
}
