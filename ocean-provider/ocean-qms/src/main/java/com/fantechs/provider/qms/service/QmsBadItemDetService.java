package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsBadItemDetDto;
import com.fantechs.common.base.general.entity.qms.QmsBadItemDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/18.
 */

public interface QmsBadItemDetService extends IService<QmsBadItemDet> {
    List<QmsBadItemDetDto> findList(Map<String, Object> map);
}
