package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtProductBomListDto;
import com.fantechs.common.base.entity.basic.SmtProductBom;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductBom;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface SmtProductBomService extends IService<SmtProductBom> {

    List<SmtProductBom> findList(SearchSmtProductBom searchSmtProductBom);

    List<SmtProductBomListDto> findProductBomList(SearchSmtProductBom searchSmtProductBom);
}
