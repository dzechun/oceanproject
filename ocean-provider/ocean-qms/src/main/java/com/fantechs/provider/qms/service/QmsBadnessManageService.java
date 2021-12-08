package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.PdaIncomingSelectToUseSubmitDto;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManage;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */

public interface QmsBadnessManageService extends IService<QmsBadnessManage> {
    List<QmsBadnessManage> findList(Map<String, Object> map);
    String checkBarcode(String barcode);
    int submit(PdaIncomingSelectToUseSubmitDto pdaIncomingSelectToUseSubmitDto);
}
