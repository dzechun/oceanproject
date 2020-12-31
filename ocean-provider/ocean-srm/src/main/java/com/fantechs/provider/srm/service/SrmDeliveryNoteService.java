package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmDeliveryNoteDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNote;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */

public interface SrmDeliveryNoteService extends IService<SrmDeliveryNote> {

    List<SrmDeliveryNoteDto> findList(Map<String, Object> map);

}
