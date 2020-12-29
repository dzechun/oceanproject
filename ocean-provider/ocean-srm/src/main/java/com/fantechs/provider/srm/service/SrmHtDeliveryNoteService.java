package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryNote;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/29.
 */

public interface SrmHtDeliveryNoteService extends IService<SrmHtDeliveryNote> {

    List<SrmHtDeliveryNote> findHtList(Map<String, Object> map);

}
