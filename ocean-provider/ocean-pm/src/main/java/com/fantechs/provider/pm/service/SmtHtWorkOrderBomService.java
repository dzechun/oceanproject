package com.fantechs.provider.pm.service;

import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */

public interface SmtHtWorkOrderBomService extends IService<SmtHtWorkOrderBom> {

    List<SmtHtWorkOrderBom> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);
}
