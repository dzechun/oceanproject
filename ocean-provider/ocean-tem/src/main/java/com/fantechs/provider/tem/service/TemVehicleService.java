package com.fantechs.provider.tem.service;

import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.dto.tem.TemVehicleImport;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.tem.history.TemHtVehicle;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface TemVehicleService extends IService<TemVehicle> {
    List<TemVehicleDto> findList(Map<String, Object> map);

    List<TemHtVehicle> findHtList(Map<String, Object> map);

    int save(TemVehicle temVehicle);

    int delete(String ids);

    int update(TemVehicle temVehicle);

    Map<String, Object> importExcel(List<TemVehicleImport> temVehicleImportList);
}
