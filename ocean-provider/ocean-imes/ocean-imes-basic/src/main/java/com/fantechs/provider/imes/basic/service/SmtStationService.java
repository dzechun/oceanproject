package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtStation;
import com.fantechs.common.base.entity.basic.search.SearchSmtStation;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/27.
 */

public interface SmtStationService extends IService<SmtStation> {

    List<SmtStation> findList(SearchSmtStation searchSmtStation);
}
