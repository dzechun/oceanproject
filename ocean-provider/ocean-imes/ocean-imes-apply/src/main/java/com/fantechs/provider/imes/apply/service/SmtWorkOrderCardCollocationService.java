package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/20.
 */

public interface SmtWorkOrderCardCollocationService extends IService<SmtWorkOrderCardCollocation> {

    List<SmtWorkOrderCardCollocation> findList(SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation);
}
