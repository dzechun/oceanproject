package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.dto.apply.SmtWorkOrderCardCollocationDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/20.
 */

public interface SmtWorkOrderCardCollocationService extends IService<SmtWorkOrderCardCollocation> {

    List<SmtWorkOrderCardCollocationDto> findList(SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation);
}
