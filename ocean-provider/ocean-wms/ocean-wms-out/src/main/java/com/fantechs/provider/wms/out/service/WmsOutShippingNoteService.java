package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNote;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutShippingNote;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2021/01/09.
 */

public interface WmsOutShippingNoteService extends IService<WmsOutShippingNote> {

    List<WmsOutShippingNoteDto> findList(Map<String, Object> dynamicConditionByEntity);

    int submit(WmsOutShippingNote wmsOutShippingNote);

    List<WmsOutShippingNote> PDAfindList(SearchWmsOutShippingNote searchWmsOutShippingNote);

    int sendMessageTest() throws Exception;
}
