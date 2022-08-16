package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseHtProductBomDetMapper extends MyMapper<BaseHtProductBomDet> {
    List<BaseHtProductBomDet> findList(SearchBaseProductBomDet searchBaseProductBomDet);
}