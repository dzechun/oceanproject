package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBomDet;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtProductBomDetMapper extends MyMapper<SmtProductBomDet> {

    List<SmtProductBomDet> findList(SearchSmtProductBomDet searchSmtProductBomDet);
    List<SmtProductBomDet> findFirstLevelProductBomDet(Long productBomId);

    List<SmtProductBomDet> findNextLevelProductBomDet(Long productBomDetId);
}