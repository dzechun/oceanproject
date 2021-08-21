package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamHtJigMaterialService extends IService<EamHtJigMaterial> {
    List<EamHtJigMaterial> findHtList(Map<String, Object> map);
}
