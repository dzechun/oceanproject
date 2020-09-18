package com.fantechs.provider.imes.basic.mapper;



import com.fantechs.common.base.entity.basic.SmtProductModel;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductModel;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtProductModelMapper extends MyMapper<SmtProductModel> {

    List<SmtProductModel> selectProductModels(SearchSmtProductModel searchSmtProductModel);
}