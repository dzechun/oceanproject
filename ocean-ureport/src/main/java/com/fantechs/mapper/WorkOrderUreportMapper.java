package com.fantechs.mapper;

import com.fantechs.dto.WorkOrderUreportDto;

import java.util.List;
import java.util.Map;

public interface WorkOrderUreportMapper {

    List<WorkOrderUreportDto> findList(Map<String, Object> map);
}
