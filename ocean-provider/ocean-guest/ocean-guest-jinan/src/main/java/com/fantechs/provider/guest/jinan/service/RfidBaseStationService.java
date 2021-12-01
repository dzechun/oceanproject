package com.fantechs.provider.guest.jinan.service;

import com.fantechs.common.base.general.dto.jinan.Import.RfidBaseStationImport;
import com.fantechs.common.base.general.entity.jinan.RfidBaseStation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface RfidBaseStationService extends IService<RfidBaseStation> {
    List<RfidBaseStation> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<RfidBaseStationImport> rfidBaseStationImports);
}
