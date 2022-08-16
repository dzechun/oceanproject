package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSampleBeforeRecheck;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/04/01.
 */

public interface QmsInspectionOrderDetSampleBeforeRecheckService extends IService<QmsInspectionOrderDetSampleBeforeRecheck> {
    List<QmsInspectionOrderDetSampleBeforeRecheck> findList(Map<String, Object> map);
}
