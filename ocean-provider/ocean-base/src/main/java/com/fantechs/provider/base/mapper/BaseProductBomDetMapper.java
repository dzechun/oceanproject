package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface BaseProductBomDetMapper extends MyMapper<BaseProductBomDet> {

    List<BaseProductBomDet> findList(SearchBaseProductBomDet searchBaseProductBomDet);
    List<BaseProductBomDet> findFirstLevelProductBomDet(Long productBomId);

    List<BaseProductBomDet> findNextLevelProductBomDet(Long productBomDetId);
}