package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;

import java.util.List;

public interface SmtProductModelService  {

    List<SmtProductModel> selectProductModels(SearchSmtProductModel searchSmtProductModel);

    int insert(SmtProductModel smtProductModel);

    int updateById(SmtProductModel smtProductModel);

    int deleteByIds(List<Long> productModelIds);

    SmtProductModel selectByKey(Long id);
}
