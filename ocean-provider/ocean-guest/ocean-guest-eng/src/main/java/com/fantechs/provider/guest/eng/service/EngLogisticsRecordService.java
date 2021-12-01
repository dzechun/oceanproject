package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.entity.eng.EngLogisticsRecord;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/03.
 */

public interface EngLogisticsRecordService extends IService<EngLogisticsRecord> {
    List<EngLogisticsRecord> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<EngLogisticsRecord> list);

    int getUnReadCount();
}