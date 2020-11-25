package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.dto.apply.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardPool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/11/21.
 */

public interface SmtWorkOrderCardPoolService extends IService<SmtWorkOrderCardPool> {

    List<SmtWorkOrderCardPoolDto> findList(SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool);
}
