package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.history.SmtHtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtProductModelMapper extends MyMapper<SmtHtProductModel> {

    List<SmtHtProductModel> selectHtProductModels(SearchSmtProductModel searchSmtProductModel);

    //int addBatchHtProductModel(List<SmtHtProductModel> list);
}