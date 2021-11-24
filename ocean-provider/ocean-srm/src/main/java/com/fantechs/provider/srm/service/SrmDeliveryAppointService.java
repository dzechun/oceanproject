package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmDeliveryAppointDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryAppoint;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/24.
 */

public interface SrmDeliveryAppointService extends IService<SrmDeliveryAppoint> {
    List<SrmDeliveryAppointDto> findList(Map<String, Object> map);

    int save(SrmDeliveryAppointDto srmDeliveryAppointDto);

    int update(SrmDeliveryAppointDto srmDeliveryAppointDto);

}
