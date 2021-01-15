package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBomDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBom;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtWorkOrderBomMapper extends MyMapper<SmtWorkOrderBom> {
    List<SmtWorkOrderBomDto> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);

    int updateBatch(List<SmtWorkOrderBom> list);
}