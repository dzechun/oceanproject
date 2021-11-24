package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.entity.srm.history.SrmHtPoExpediteRecord;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/18.
 */

public interface SrmHtPoExpediteRecordService extends IService<SrmHtPoExpediteRecord> {
    List<SrmHtPoExpediteRecord> findList(Map<String, Object> map);
}
