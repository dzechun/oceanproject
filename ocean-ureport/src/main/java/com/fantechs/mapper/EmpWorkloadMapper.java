package com.fantechs.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.dto.EmpWorkload;
import com.fantechs.entity.search.SearchEmpWorkload;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpWorkloadMapper extends MyMapper<EmpWorkload> {

    List<Map<String, Object>> findHistogram(SearchEmpWorkload searchEmpWorkload);

    List<EmpWorkload> findHistogramList(SearchEmpWorkload searchEmpWorkload);
}
