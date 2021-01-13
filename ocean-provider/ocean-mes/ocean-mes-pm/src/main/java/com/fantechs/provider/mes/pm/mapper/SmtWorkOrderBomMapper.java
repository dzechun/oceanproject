package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.dto.apply.SmtWorkOrderBomDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtWorkOrderBomMapper extends MyMapper<SmtWorkOrderBom> {
    List<SmtWorkOrderBomDto> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);

    int updateBatch(List<SmtWorkOrderBom> list);
}