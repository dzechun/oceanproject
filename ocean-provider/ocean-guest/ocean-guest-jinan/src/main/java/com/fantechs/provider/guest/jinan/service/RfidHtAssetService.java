package com.fantechs.provider.guest.jinan.service;

import com.fantechs.common.base.general.entity.jinan.history.RfidHtAsset;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface RfidHtAssetService extends IService<RfidHtAsset> {
    List<RfidHtAsset> findHtList(Map<String, Object> map);

}
