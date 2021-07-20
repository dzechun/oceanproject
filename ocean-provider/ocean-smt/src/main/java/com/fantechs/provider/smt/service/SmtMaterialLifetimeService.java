package com.fantechs.provider.smt.service;

import com.fantechs.common.base.general.dto.smt.SmtMaterialLifetimeDto;
import com.fantechs.common.base.general.entity.smt.SmtMaterialLifetime;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/20.
 */

public interface SmtMaterialLifetimeService extends IService<SmtMaterialLifetime> {
    List<SmtMaterialLifetimeDto> findList(Map<String, Object> map);
}
