package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtProductBom;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBom;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBom;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtProductBomMapper extends MyMapper<SmtHtProductBom> {
    List<SmtProductBom> findList(SearchSmtProductBom searchSmtProductBom);
}