package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtWorkOrderBomMapper extends MyMapper<SmtHtWorkOrderBom> {
    List<SmtHtWorkOrderBom> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);
}