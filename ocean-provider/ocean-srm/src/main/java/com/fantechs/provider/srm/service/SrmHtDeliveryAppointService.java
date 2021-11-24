package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryAppoint;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */

public interface SrmHtDeliveryAppointService extends IService<SrmHtDeliveryAppoint> {
    List<SrmHtDeliveryAppoint> findList(Map<String, Object> map);
}
