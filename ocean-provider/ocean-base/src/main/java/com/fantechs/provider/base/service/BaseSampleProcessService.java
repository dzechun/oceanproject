package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */

public interface BaseSampleProcessService extends IService<BaseSampleProcess> {
    List<BaseSampleProcess> findList(Map<String, Object> map);
    List<BaseSampleProcess> findListByIds(String ids);
    BaseSampleProcess getAcReQty(Long sampleProcessId, BigDecimal orderQty);
}
