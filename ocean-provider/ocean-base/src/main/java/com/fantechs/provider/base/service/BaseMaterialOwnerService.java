package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/22.
 */

public interface BaseMaterialOwnerService extends IService<BaseMaterialOwner> {
    List<BaseMaterialOwnerDto> findList(Map<String, Object> map);
    List<BaseMaterialOwnerDto> findAll();
    Map<String, Object> importExcel(List<BaseMaterialOwnerDto> baseMaterialOwnerDtos);
}
