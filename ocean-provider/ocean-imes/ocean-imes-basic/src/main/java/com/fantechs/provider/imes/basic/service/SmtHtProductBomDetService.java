package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.SmtHtProductBomDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBomDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface SmtHtProductBomDetService extends IService<SmtHtProductBomDet> {

    List<SmtHtProductBomDet> findList(SearchSmtProductBomDet searchSmtProductBomDet);
}
