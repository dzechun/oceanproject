package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamSparePartDto;
import com.fantechs.common.base.general.entity.eam.EamSparePart;
import com.fantechs.common.base.general.entity.eam.history.EamHtSparePart;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/17.
 */

public interface EamSparePartService extends IService<EamSparePart> {
    List<EamSparePartDto> findList(Map<String, Object> map);

    List<EamHtSparePart> findHtList(Map<String, Object> map);
}
