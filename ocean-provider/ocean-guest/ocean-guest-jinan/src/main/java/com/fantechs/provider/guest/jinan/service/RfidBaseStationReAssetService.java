package com.fantechs.provider.guest.jinan.service;

import com.fantechs.common.base.general.entity.jinan.RfidBaseStationReAsset;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/30.
 */

public interface RfidBaseStationReAssetService extends IService<RfidBaseStationReAsset> {
    List<RfidBaseStationReAsset> findList(Map<String, Object> map);
}
