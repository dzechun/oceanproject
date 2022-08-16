package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.history.BaseHtProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseHtProductModelMapper extends MyMapper<BaseHtProductModel> {

    List<BaseHtProductModel> selectHtProductModels(Map<String, Object> map);

    //int addBatchHtProductModel(List<SmtHtProductModel> list);
}