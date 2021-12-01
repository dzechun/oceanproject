package com.fantechs.provider.guest.jinan.service;

import com.fantechs.common.base.general.entity.jinan.RfidBaseStationData;
import com.fantechs.common.base.general.entity.jinan.RfidBaseStationLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface RfidBaseStationLogService extends IService<RfidBaseStationLog> {
    List<RfidBaseStationLog> findList(Map<String, Object> map);

    int checkData(RfidBaseStationData rfidBaseStationData);
}
