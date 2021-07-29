package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigReMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamHtJigReMaterialService extends IService<EamHtJigReMaterial> {
    List<EamHtJigReMaterial> findHtList(Map<String, Object> map);
}
