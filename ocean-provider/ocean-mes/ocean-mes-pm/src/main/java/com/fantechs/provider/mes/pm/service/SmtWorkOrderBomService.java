package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBomDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBom;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */

public interface SmtWorkOrderBomService extends IService<SmtWorkOrderBom> {

    List<SmtWorkOrderBomDto> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);
    //新增或更新工单BOM
    int save(List<SmtWorkOrderBom> smtWorkOrderBomList);
}
