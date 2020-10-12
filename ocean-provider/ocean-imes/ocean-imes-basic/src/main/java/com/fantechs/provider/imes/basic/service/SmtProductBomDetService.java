package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBomDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface SmtProductBomDetService extends IService<SmtProductBomDet> {

    List<SmtProductBomDet> findList(SearchSmtProductBomDet searchSmtProductBomDet);
}
