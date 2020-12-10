package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.SmtPaddingMaterialDto;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtPaddingMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/10.
 */

public interface SmtPaddingMaterialService extends IService<SmtPaddingMaterial> {

    List<SmtPaddingMaterialDto> findList(Map<String, Object> map);
}
