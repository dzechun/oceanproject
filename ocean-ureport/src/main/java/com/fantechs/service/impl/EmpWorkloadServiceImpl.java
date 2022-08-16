package com.fantechs.service.impl;

import com.fantechs.common.base.support.BaseService;
import com.fantechs.dto.EmpWorkload;
import com.fantechs.entity.search.SearchEmpWorkload;
import com.fantechs.mapper.EmpWorkloadMapper;
import com.fantechs.service.EmpWorkloadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/03/08.
 */
@Service
public class EmpWorkloadServiceImpl extends BaseService<EmpWorkload> implements EmpWorkloadService {
    @Resource
    private EmpWorkloadMapper empWorkloadMapper;

    @Override
    public List<Map<String, Object>> findHistogram(Map<String, Object> map) {
        return empWorkloadMapper.findHistogram(map);
    }

    @Override
    public List<EmpWorkload> findHistogramList(Map<String, Object> map) {

        return empWorkloadMapper.findHistogramList(map);
    }

}
