package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */

public interface SmtWorkOrderBomService extends IService<SmtWorkOrderBom> {

    List<SmtWorkOrderBom> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom);
}
