package com.fantechs.service;

import com.fantechs.dto.WorkOrderUreportDto;

import java.util.List;
import java.util.Map;

public interface WorkOrderUreportService {

    List<WorkOrderUreportDto> findList(Map<String, Object> map);
}
