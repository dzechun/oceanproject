package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmDeliveryNoteDetDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNoteDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */

public interface SrmDeliveryNoteDetService extends IService<SrmDeliveryNoteDet> {

    List<SrmDeliveryNoteDetDto> findList(Map<String, Object> map);
}
