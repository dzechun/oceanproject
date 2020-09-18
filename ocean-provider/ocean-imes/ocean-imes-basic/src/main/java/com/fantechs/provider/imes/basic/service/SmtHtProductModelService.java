package com.fantechs.provider.imes.basic.service;


import com.fantechs.common.base.entity.basic.history.SmtHtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;

import java.util.List;

public interface SmtHtProductModelService {

    List<SmtHtProductModel> selectHtProductModels(SearchSmtProductModel searchSmtProductModel);
}
