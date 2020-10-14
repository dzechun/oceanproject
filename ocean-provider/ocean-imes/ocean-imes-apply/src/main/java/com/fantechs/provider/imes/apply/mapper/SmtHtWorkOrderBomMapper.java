package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtWorkOrderBomMapper extends MyMapper<SmtHtWorkOrderBom> {
    List<SmtWorkOrderBom> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);
}