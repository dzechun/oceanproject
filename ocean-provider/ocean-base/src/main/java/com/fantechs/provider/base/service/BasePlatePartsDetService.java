package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */

public interface BasePlatePartsDetService extends IService<BasePlatePartsDet> {
    List<BasePlatePartsDetDto> findList(Map<String, Object> map);
}
