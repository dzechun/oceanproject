package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface SmtProductModelService extends IService<SmtProductModel> {

    List<SmtProductModel> selectProductModels(SearchSmtProductModel searchSmtProductModel);

    int insert(SmtProductModel smtProductModel);

    int updateById(SmtProductModel smtProductModel);

    int deleteByIds(List<Long> productModelIds);

    SmtProductModel selectByKey(Long id);
}
