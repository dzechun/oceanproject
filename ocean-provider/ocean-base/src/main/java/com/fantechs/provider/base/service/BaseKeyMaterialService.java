package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseKeyMaterialDto;
import com.fantechs.common.base.general.entity.basic.BaseKeyMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/24.
 */

public interface BaseKeyMaterialService extends IService<BaseKeyMaterial> {

    List<BaseKeyMaterialDto> findList(Map<String, Object> map);

}
