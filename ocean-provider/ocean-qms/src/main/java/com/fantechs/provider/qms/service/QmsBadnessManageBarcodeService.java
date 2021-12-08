package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsBadnessManageBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */

public interface QmsBadnessManageBarcodeService extends IService<QmsBadnessManageBarcode> {
    List<QmsBadnessManageBarcode> findList(Map<String, Object> map);
}
