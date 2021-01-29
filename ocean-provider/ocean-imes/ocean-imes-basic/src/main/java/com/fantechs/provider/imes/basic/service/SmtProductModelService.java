package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface SmtProductModelService extends IService<SmtProductModel> {

    List<SmtProductModel> selectProductModels(SearchSmtProductModel searchSmtProductModel);
    Map<String, Object> importExcel(List<SmtProductModel> smtProductModels);
}
