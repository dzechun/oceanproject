package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.history.BaseHtProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtProductModelMapper extends MyMapper<BaseHtProductModel> {

    List<BaseHtProductModel> selectHtProductModels(SearchBaseProductModel searchBaseProductModel);

    //int addBatchHtProductModel(List<SmtHtProductModel> list);
}