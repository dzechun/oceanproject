package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */

public interface WmsOutShippingNoteDetService extends IService<WmsOutShippingNoteDet> {

    List<WmsOutShippingNoteDetDto> findList(Map<String, Object> dynamicConditionByEntity);
}
