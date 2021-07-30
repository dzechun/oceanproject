package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamJigReMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamJigReMaterialService extends IService<EamJigReMaterial> {
    List<EamJigReMaterialDto> findList(Map<String, Object> map);

    int batchAddOrUpdate(List<EamJigReMaterial> list);
}
