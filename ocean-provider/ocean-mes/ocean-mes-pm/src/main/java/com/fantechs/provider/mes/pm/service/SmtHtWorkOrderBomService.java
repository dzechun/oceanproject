package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */

public interface SmtHtWorkOrderBomService extends IService<SmtHtWorkOrderBom> {

    List<SmtHtWorkOrderBom> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);
}
