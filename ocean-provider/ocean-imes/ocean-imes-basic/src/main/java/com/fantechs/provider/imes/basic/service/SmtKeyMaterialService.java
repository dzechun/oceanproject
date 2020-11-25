package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtKeyMaterialDto;
import com.fantechs.common.base.entity.basic.SmtKeyMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/24.
 */

public interface SmtKeyMaterialService extends IService<SmtKeyMaterial> {

    List<SmtKeyMaterialDto> findList(Map<String, Object> map);

}
