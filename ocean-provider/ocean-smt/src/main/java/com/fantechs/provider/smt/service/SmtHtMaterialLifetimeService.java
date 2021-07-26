package com.fantechs.provider.smt.service;

import com.fantechs.common.base.general.entity.smt.history.SmtHtMaterialLifetime;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/20.
 */

public interface SmtHtMaterialLifetimeService extends IService<SmtHtMaterialLifetime> {
    List<SmtHtMaterialLifetime> findHtList(Map<String, Object> map);
}
