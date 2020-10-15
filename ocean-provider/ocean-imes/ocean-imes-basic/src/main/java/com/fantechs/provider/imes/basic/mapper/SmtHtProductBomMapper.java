package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtProductBom;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBom;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtProductBomMapper extends MyMapper<SmtHtProductBom> {
    List<SmtHtProductBom> findList(SearchSmtProductBom searchSmtProductBom);
}