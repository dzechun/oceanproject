package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmHtAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtAppointDeliveryReAsn;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */

public interface SrmHtAppointDeliveryReAsnService extends IService<SrmHtAppointDeliveryReAsn> {
    List<SrmHtAppointDeliveryReAsnDto> findList(Map<String, Object> map);
}
