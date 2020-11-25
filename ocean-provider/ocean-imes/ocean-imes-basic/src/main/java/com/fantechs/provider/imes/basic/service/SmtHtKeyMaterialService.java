package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtKeyMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtKeyMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/24.
 */

public interface SmtHtKeyMaterialService extends IService<SmtHtKeyMaterial> {

    List<SmtHtKeyMaterial> findHtList(Map<String, Object> map);
}
