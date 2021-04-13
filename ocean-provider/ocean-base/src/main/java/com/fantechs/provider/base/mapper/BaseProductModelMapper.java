package com.fantechs.provider.base.mapper;



import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseProductModelMapper extends MyMapper<BaseProductModel> {

    List<BaseProductModel> selectProductModels(SearchBaseProductModel searchBaseProductModel);
}