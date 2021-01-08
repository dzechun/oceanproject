package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtProductBomDto;
import com.fantechs.common.base.entity.basic.SmtProductBom;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface SmtProductBomService extends IService<SmtProductBom> {

    List<SmtProductBomDto> findList(Map<String,Object> map);
}
