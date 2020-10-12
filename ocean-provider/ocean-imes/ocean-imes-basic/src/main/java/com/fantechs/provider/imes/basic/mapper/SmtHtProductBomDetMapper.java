package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBomDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBomDet;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtProductBomDetMapper extends MyMapper<SmtHtProductBomDet> {
    List<SmtProductBomDet> findList(SearchSmtProductBomDet searchSmtProductBomDet);
}