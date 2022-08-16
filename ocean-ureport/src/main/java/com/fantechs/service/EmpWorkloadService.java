package com.fantechs.service;

import com.fantechs.common.base.support.IService;
import com.fantechs.dto.EmpWorkload;

import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/03/08.
 */

public interface EmpWorkloadService extends IService<EmpWorkload> {

    List<Map<String, Object>> findHistogram(Map<String, Object> map);

    List<EmpWorkload> findHistogramList(Map<String, Object> map);
}
