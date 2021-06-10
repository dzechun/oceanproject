package com.fantechs.provider.tem.service;

import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/08.
 */

public interface TemVehicleService extends IService<TemVehicle> {
    List<TemVehicleDto> findList(Map<String, Object> map);
}
