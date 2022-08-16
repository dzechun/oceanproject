package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtShipmentEnterprise;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/16.
 */

public interface BaseHtShipmentEnterpriseService extends IService<BaseHtShipmentEnterprise> {

    List<BaseHtShipmentEnterprise> findHtList(Map<String, Object> map);
}
