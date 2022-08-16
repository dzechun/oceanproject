package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPartsInformation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/14.
 */

public interface BaseHtPartsInformationService extends IService<BaseHtPartsInformation> {
    List<BaseHtPartsInformation> findHtList(Map<String, Object> map);
}
